__author__ = 'mc'


class Order:
    """ Base class for BuyOrder/SellOrder."""
    trader_id = 0
    price = 0
    stocks = 0

    def __init__(self, trader_id, price, stocks):
        self.trader_id = int(trader_id)
        self.price = float(price)
        self.stocks = int(stocks)


class BuyOrder(Order):
    """ Represents an order to buy a stock.
    trader_id: ID of the trader that wants to buy a stock.
    price: Proposed price of one stock.
    stocks: Number of the stocks that the trader wants to buy.
    """

    def __str__(self):
        return "[BuyOrder trader: " + str(self.trader_id) + " price: " + str(self.price) + " stocks: " + str(self.stocks) + "]"


class SellOrder(Order):
    """ Represents an order to sell a stock.
    trader_id: ID of the trader that wants to sell a stock.
    price: Proposed price of one stock.
    stocks: Number of the stocks that the trader wants to sell.
    """

    def __str__(self):
        return "[SellOrder trader: " + str(self.trader_id) + " price: " + str(self.price) + " stocks: " + str(self.stocks) + "]"
