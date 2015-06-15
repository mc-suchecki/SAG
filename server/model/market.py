from model.offer import PurchaseOffer, SaleOffer
from model.stock import Stock
from model.trader import Trader
import logging

__author__ = 'mc'

DEFAULT_TICKER = "GOOG"


class Market:
    """ Class representing the stock exchange market model. """
    stock = None
    traders = []
    purchase_offers = []
    sale_offers = []

    def __init__(self):
        self.stock = Stock(DEFAULT_TICKER)
        self.traders = []
        self.purchase_offers = []
        self.sale_offers = []

    def add_trader(self):
        """ Adds new trader to the market. """
        trader = Trader(len(self.traders) + 1)
        self.traders.append(trader)
        logging.info("New Trader registered! " + str(trader))
        return trader

    def get_stock_price(self, day):
        """ Returns historical stock price in a given day. """
        return self.stock.price_history[day]

    def place_purchase_offer(self, trader_id, price, stocks):
        """ Places an offer to buy a stock.
        :param trader_id: ID of the trader that wants to buy a stock.
        :param price: Proposed price of one stock.
        :param stocks: Number of the stocks that the trader wants to buy.
        """
        offer = PurchaseOffer(trader_id, price, stocks)
        self.purchase_offers.append(offer)
        logging.info("New Offer registered! " + str(offer))

    def place_sale_offer(self, trader_id, price, stocks):
        """ Places an offer to purchase a stock.
        :param trader_id: ID of the trader that wants to sell a stock.
        :param price: Proposed price of one stock.
        :param stocks: Number of the stocks that the trader wants to sell.
        """
        offer = SaleOffer(trader_id, price, stocks)
        self.purchase_offers.append(offer)
        logging.info("New Offer registered! " + str(offer))

