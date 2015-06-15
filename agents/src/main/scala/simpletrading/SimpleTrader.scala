package simpletrading

import common.{AbstractTrader, Init}

/**
 * Class representing simple trader which doesn't communicate with other agents. The only information it uses for
 * buy/sell decision making is stock price history.
 */
class SimpleTrader extends AbstractTrader {
  override def receive: Receive = {
    case Init =>
      println("SimpleTrader INIT")
      register()

      //TODO get history of prices
      getHistory
      trade()
  }

  def getHistory = {
    //TODO
    null
  }

  def trade() = {
    //TODO
  }

}
