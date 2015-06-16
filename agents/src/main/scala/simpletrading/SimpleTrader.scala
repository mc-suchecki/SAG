package simpletrading

import common.{AbstractTrader, Init}

/**
 * Class representing simple trader which doesn't communicate with other agents. The only information it uses for
 * buy/sell decision making is stock price history.
 */
class SimpleTrader extends AbstractTrader {
  override def receive: Receive = {
    case Init =>
      register()
      trade()
  }


  def trade() = {
    //TODO trading in loop

    var day = downloadCurrentDay()
    val price = downloadPrice(day - 1)

    val quantityToBuy = (cash/price).toInt
    if(quantityToBuy > 0)
    {
      buy(price, quantityToBuy)
      println("BUY")
    }
    else
    {
      sell(price, stocks)
      println("SELL")
    }
  }

}
