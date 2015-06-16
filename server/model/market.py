from model.order import BuyOrder, SellOrder
from model.stock import Stock
from model.trader import Trader
from threading import Timer
import logging

__author__ = 'mc'

DEFAULT_TICKER = "GOOG"
SESSION_LENGTH = 5


class Market:
    """ Class representing the stock exchange market model. """
    stock = None
    traders = []
    buy_orders = []
    sell_orders = []
    current_iteration = 0

    def __init__(self):
        self.stock = Stock(DEFAULT_TICKER)
        self.current_iteration = len(self.stock.price_history) + 1
        self.traders = []
        self.buy_orders = []
        self.sell_orders = []

    def add_trader(self):
        """ Adds new trader to the market. """
        trader = Trader(len(self.traders) + 1)
        self.traders.append(trader)
        logging.info("New Trader registered! " + str(trader))
        return trader

    def get_trader_status(self, trader_id):
        """ Returns current status of a trader. """
        return self.traders[int(trader_id) - 1]

    def get_stock_price(self, day):
        """ Returns historical stock price in a given day. """
        return self.stock.price_history[day]

    def get_stock_price_history(self):
        """ Returns whole prices history of a stock. """
        return self.stock.price_history

    def place_buy_order(self, trader_id, price, stocks):
        """ Places an order to buy a stock.
        :param trader_id: ID of the trader that wants to buy a stock.
        :param price: Proposed price of one stock.
        :param stocks: Number of the stocks that the trader wants to buy.
        """
        # TODO check if the offer is valid (trader exists, price and stocks over 0 and so on)
        offer = BuyOrder(trader_id, price, stocks)
        self.buy_orders.append(offer)
        logging.info("New Offer registered! " + str(offer))

    def place_sell_order(self, trader_id, price, stocks):
        """ Places an order to sell a stock.
        :param trader_id: ID of the trader that wants to sell a stock.
        :param price: Proposed price of one stock.
        :param stocks: Number of the stocks that the trader wants to sell.
        """
        # TODO check if the offer is valid (trader exists, price and stocks over 0 and so on)
        offer = SellOrder(trader_id, price, stocks)
        self.sell_orders.append(offer)
        logging.info("New Offer registered! " + str(offer))

    def market_session(self, continuous):
        """ Makes one step of a market session and schedules the next one if continuous=True was passed. """
        # sort the orders by price
        buy_orders = sorted(self.buy_orders, key=lambda order: -order.price)
        sell_orders = sorted(self.sell_orders, key=lambda order: order.price)

        # match the orders and update traders state
        # TODO refactor this and add logging
        orders_executed = 0
        prices_sum = 0
        while buy_orders and sell_orders and buy_orders[0].price >= sell_orders[0].price:
            orders_executed += 1
            buyer_id = buy_orders[0].trader_id
            seller_id = sell_orders[0].trader_id
            bid_price = buy_orders[0].price
            ask_price = sell_orders[0].price
            price = (bid_price + ask_price) / 2
            prices_sum += price
            stocks_to_buy = buy_orders[0].stocks
            stocks_to_sell = sell_orders[0].stocks
            # orders have matching number of stocks - remove both offers
            if stocks_to_buy == stocks_to_sell:
                buy_orders.pop(0)
                sell_orders.pop(0)
                # execute the orders completely
                self.traders[buyer_id - 1].cash -= stocks_to_buy * price
                self.traders[buyer_id - 1].stocks += stocks_to_buy
                self.traders[seller_id - 1].cash += stocks_to_buy * price
                self.traders[seller_id - 1].stocks -= stocks_to_buy
            # more stocks to sell than to buy - remove buy order and update sell order
            elif stocks_to_sell > stocks_to_buy:
                buy_orders.pop(0)
                sell_orders[0].stocks -= stocks_to_buy
                # execute the orders partially
                self.traders[buyer_id - 1].cash -= stocks_to_buy * price
                self.traders[buyer_id - 1].stocks += stocks_to_buy
                self.traders[seller_id - 1].cash += stocks_to_buy * price
                self.traders[seller_id - 1].stocks -= stocks_to_buy
            # more stocks to buy than to sell - remove sell order and update buy order
            else:
                sell_orders.pop(0)
                buy_orders[0].stocks -= stocks_to_sell
                # execute the orders partially
                self.traders[buyer_id - 1].cash -= stocks_to_sell * price
                self.traders[buyer_id - 1].stocks += stocks_to_sell
                self.traders[seller_id - 1].cash += stocks_to_sell * price
                self.traders[seller_id - 1].stocks -= stocks_to_sell

        # remove the orders that can not be executed
        self.buy_orders.clear()
        self.sell_orders.clear()

        # update the variables
        self.current_iteration += 1
        if orders_executed != 0:
            self.stock.price_history.append(prices_sum / orders_executed)
        else:
            self.stock.price_history.append(self.stock.price_history[-1])

        # schedule the next market session when SESSION_LENGTH will pass
        if continuous:
            Timer(SESSION_LENGTH, self.market_session, (True,)).start()
