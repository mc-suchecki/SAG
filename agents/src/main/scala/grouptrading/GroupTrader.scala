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
    case Buy(partOfCashToSpend: Double, price: Double) =>
      tryToBuy(price, partOfCashToSpend)
    case Sell(partOfStocksToSell: Double, price: Double) =>
      tryToSell(price, partOfStocksToSell)
  }

  def tryToBuy(price: Double, partOfCashToSpend: Double): Unit = {
    val quantity = (cash * partOfCashToSpend/price).toInt
    buy(price, quantity)
  }

  def tryToSell(price: Double, partOfStocksToSell: Double): Unit = {
    val quantity = (stocks * partOfStocksToSell).toInt
    sell(price, quantity)
  }
}
