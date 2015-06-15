""" Main web server file - defines the server REST API. """
__author__ = 'mc'

from server.model.market import Market
from flask import Flask, jsonify, request


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
        self.app.add_url_rule('/traders', 'traders', self.register_trader, methods=['POST'])
        self.app.add_url_rule('/price', 'price', self.get_stock_price, methods=['GET'])

    def main_page(self):
        """ Displays webpage with various statistics. """
        return "Number of Traders registered so far: " + str(self.market.traders_count)

    def register_trader(self):
        """ Registers a new Trader - returns trader ID, cash amount and stocks number. """
        trader = self.market.add_trader()
        return jsonify({'id': trader.id, 'cash': trader.cash, 'stocks': trader.stocks})

    def get_stock_price(self):
        """ Returns historical stock price. """
        day = request.args["day"]
        return jsonify({'price': float(self.market.get_stock_price(int(day)))})
