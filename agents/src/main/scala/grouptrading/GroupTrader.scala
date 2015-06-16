package grouptrading

import common.{AbstractTrader, Init}

/**
 * Class representing group trader which doesn't make buy/sell decisions on its own but rather takes commands from
 * it's Master.
 */
class GroupTrader extends AbstractTrader {

  override def receive: Receive = {
    case Init =>
      register()
    case Buy(partOfCashToSpend: Double) =>
      buy(partOfCashToSpend)
    case Sell(partOfStocksToSell: Double) =>
      sell(partOfStocksToSell)
  }

  def buy(partOfCashToSpend: Double): Unit = {
      //TODO
  }

  def sell(partOfStocksToSell: Double): Unit = {
      //TODO
  }
}
