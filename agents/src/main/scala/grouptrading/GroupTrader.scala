package grouptrading

import common.{AbstractTrader, Init}

/**
 * Class representing group trader which doesn't make buy/sell decisions on its own but rather takes commands from
 * it's Master.
 */
class GroupTrader extends AbstractTrader {
  override def receive: Receive = {
    case Init =>
      println("GroupTrader INIT")
      register()
    case Buy =>
      buy()
    case Sell =>
      sell()
  }

  def buy() = {
      println("GroupTrader BUY")
      //TODO
  }

  def sell() = {
      println("GroupTrader SELL")
      //TODO
  }
}
