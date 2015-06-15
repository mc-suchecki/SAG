__author__ = 'mc'


class Offer:
    """ Base class for PurchaseOffer/SaleOffer."""
    trader_id = 0
    price = 0
    stocks = 0

    def __init__(self, trader_id, price, stocks):
        self.trader_id = trader_id
        self.price = price
        self.stocks = stocks


class PurchaseOffer(Offer):
    """ Represents an offer to buy a stock.
    trader_id: ID of the trader that wants to buy a stock.
    price: Proposed price of one stock.
    stocks: Number of the stocks that the trader wants to buy.
    """

    def __str__(self):
        return "[PurchaseOffer trader: " + self.trader_id + " price: " + self.price + " stocks: " + self.stocks + "]"


class SaleOffer(Offer):
    """ Represents an offer to sell a stock.
    trader_id: ID of the trader that wants to sell a stock.
    price: Proposed price of one stock.
    stocks: Number of the stocks that the trader wants to sell.
    """

    def __str__(self):
        return "[SaleOffer trader: " + self.trader_id + " price: " + self.price + " stocks: " + self.stocks + "]"
