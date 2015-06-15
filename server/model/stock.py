__author__ = 'mc'

from yahoo_finance import Share
from datetime import date, timedelta

# downloading history from last 42 days to have at least 26 historical prices
# (NASDAQ exchange is not working during weekends!)
HISTORY_LENGTH = 42


class Stock:
    ticker = ""
    price_history = []

    def __init__(self, ticker):
        self.ticker = ticker
        share = Share(ticker)
        today = date.today().isoformat()
        month_ago = (date.today() - timedelta(HISTORY_LENGTH)).isoformat()
        print("Getting history for stock " + ticker + " from " + month_ago + " to " + today + "... ", end="")
        history = share.get_historical(month_ago, today)
        print("OK - " + str(len(history)) + " prices downloaded.")
        for item in reversed(history):
            self.price_history.append(item["Close"])
