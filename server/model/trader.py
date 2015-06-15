__author__ = 'mc'

# initial parameters
INITIAL_CASH = 100000
INITIAL_STOCKS = 100


class Trader:
    """ Trader represents an entity that is able to place offers on the market. """
    id = 0
    stocks = INITIAL_STOCKS
    cash = INITIAL_CASH

    def __init__(self, trader_id):
        self.id = trader_id
        self.cash = INITIAL_CASH
        self.stocks = INITIAL_STOCKS

    def __str__(self):
        return '[Trader ID: %s cash: %s]' % (self.id, self.cash)