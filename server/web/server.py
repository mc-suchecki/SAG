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
        self.app.add_url_rule('/stock/buy', 'buy', self.place_purchase_offer, methods=['POST'])
        self.app.add_url_rule('/stock/sell', 'sell', self.place_sale_offer, methods=['POST'])

    def main_page(self):
        """ Displays webpage with simulation visualisation. """
        return render_template("index.html",
                               traders_count=len(self.market.traders),
                               current_iteration=self.market.current_iteration,
                               traders=self.market.traders)

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

    def place_purchase_offer(self):
        """ Allows traders to place offers to buy a stock. """
        price = request.args["price"]
        stocks = request.args["stocks"]
        trader_id = request.args["trader_id"]
        self.market.place_purchase_offer(trader_id, price, stocks)
        return ""

    def place_sale_offer(self):
        """ Allows traders to place offers to sell a stock. """
        price = request.args["price"]
        stocks = request.args["stocks"]
        trader_id = request.args["trader_id"]
        self.market.place_sale_offer(trader_id, price, stocks)
        return ""
