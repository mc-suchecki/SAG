__author__ = 'mc'

from server.model.trader import Trader
import logging


class Market:
    """ Class representing the stock exchange market model. """
    traders = []
    traders_count = 0

    def __init__(self):
        self.traders = []
        self.traders_count = 0

    def add_trader(self):
        """ Adds new trader to the market. """
        self.traders_count += 1
        trader = Trader(self.traders_count)
        self.traders.append(trader)
        logging.info("New Trader registered! " + str(trader))
        return trader
