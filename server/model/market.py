from model.stock import Stock
from model.trader import Trader
import logging

__author__ = 'mc'

DEFAULT_TICKER = "GOOG"


class Market:
    """ Class representing the stock exchange market model. """
    stock = None
    traders = []
    offers = []

    def __init__(self):
        self.traders = []
        self.stock = Stock(DEFAULT_TICKER)

    def add_trader(self):
        """ Adds new trader to the market. """
        trader = Trader(len(self.traders) + 1)
        self.traders.append(trader)
        logging.info("New Trader registered! " + str(trader))
        return trader

    def get_stock_price(self, day):
        """ Returns historical stock price in a given day. """
        return self.stock.price_history[day]

