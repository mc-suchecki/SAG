""" Main web server file - defines the server REST API. """
__author__ = 'mc'

from model.market import Market
from flask import Flask, jsonify, request, render_template


class WebServer():
    """ Responsible for communication with traders and displaying stats webpage. """
    market = None
    app = None

    def __init__(self):
        self.market = Market()
        self.app = Flask(__name__)
        self.__create_routes__()

    def run(self):
        """ Runs the web server that is accessible from outside on localhost. """
        self.app.run(host="0.0.0.0")

    def __create_routes__(self):
        """ Attaches the REST URIs to class methods. """
        self.app.add_url_rule('/', 'main_page', self.main_page)
        self.app.add_url_rule('/day', 'day', self.get_current_iteration, methods=['GET'])
        self.app.add_url_rule('/traders', 'traders', self.register_trader, methods=['POST'])
        self.app.add_url_rule('/traders/<id>', 'trader', self.get_trader_state, methods=['GET'])
        self.app.add_url_rule('/stock/price', 'price', self.get_stock_price, methods=['GET'])
        self.app.add_url_rule('/stock/history', 'history', self.get_stock_price_history, methods=['GET'])
        self.app.add_url_rule('/stock/buy', 'buy', self.place_buy_order, methods=['POST'])
        self.app.add_url_rule('/stock/sell', 'sell', self.place_sell_order, methods=['POST'])
        self.app.add_url_rule('/simulation/step', 'step', self.market_session_step, methods=['POST'])
        self.app.add_url_rule('/simulation/run', 'run', self.run_simulation, methods=['POST'])

    def main_page(self):
        """ Displays webpage with simulation visualisation. """
        return render_template("index.html", traders_count=len(self.market.traders),
                               current_iteration=self.market.current_iteration, traders=self.market.traders,
                               buy_orders=self.market.buy_orders, sell_orders=self.market.sell_orders,
                               current_stock_price=self.market.stock.price_history[-1])

    def get_current_iteration(self):
        """ Returns number of the current iteration. """
        return jsonify({"day": int(self.market.current_iteration)})

    def register_trader(self):
        """ Registers a new Trader - returns trader ID, cash amount and stocks number. """
        trader = self.market.add_trader()
        return jsonify({'id': trader.id, 'cash': trader.cash, 'stocks': trader.stocks})

    def get_trader_state(self, id):
        """ Returns current state of a trader - number of currently owned stocks and cash.
        :param id: ID of the Trader
        :return: all available and up-to-date data about Trader
        """
        trader = self.market.get_trader_status(id)
        return jsonify(trader.__dict__)

    def get_stock_price(self):
        """ Returns historical stock price. """
        day = request.args["day"]
        return jsonify({'price': float(self.market.get_stock_price(int(day)))})

    def get_stock_price_history(self):
        """ Returns all historical stock prices. """
        return jsonify({'price_history': self.market.get_stock_price_history()})

    def place_buy_order(self):
        """ Allows traders to place orders to buy a stock. """
        price = request.args["price"]
        stocks = request.args["stocks"]
        trader_id = request.args["trader_id"]
        self.market.place_buy_order(trader_id, price, stocks)
        return ""

    def place_sell_order(self):
        """ Allows traders to place orders to sell a stock. """
        price = request.args["price"]
        stocks = request.args["stocks"]
        trader_id = request.args["trader_id"]
        self.market.place_sell_order(trader_id, price, stocks)
        return ""

    def market_session_step(self):
        """ Executes all of the placed orders in current session. """
        self.market.market_session(False)
        return ""

    def run_simulation(self):
        """ Does one market session step and schedules next ones. """
        self.market.market_session(True)
        return ""
