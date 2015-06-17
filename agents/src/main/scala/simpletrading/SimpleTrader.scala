package simpletrading

import java.util.concurrent.TimeUnit

import common.{Trade, AbstractTrader, AgentMessage, Init}

import scala.concurrent.duration.FiniteDuration
import scala.util.Random

/**
 * Class representing simple trader which doesn't communicate with other agents. The only information it uses for
 * buy/sell decision making is last stock price.
 */
class SimpleTrader extends AbstractTrader {
  import context._

  val randomNumberGenerator = new Random()

  override def receive: Receive = {
    case Init =>
      register()
      val initialDelay = new FiniteDuration(0, TimeUnit.SECONDS)
      val interval = new FiniteDuration(5, TimeUnit.SECONDS)

      context.system.scheduler.schedule(initialDelay, interval, self, Trade)

    case Trade =>
      val prevCurrDay = currentDay
      updateCurrentDay()
      if(prevCurrDay != currentDay)
      {
        updateState()
        trade()
      }
      else
      {
        //println("Still the same day")
      }
  }

  private def trade(): Unit = {
    val randomDelta = randomNumberGenerator.nextFloat()*10-5 //random number from [-5,5]
    val price = downloadPrice(currentDay - 1) + randomDelta

    if((id+currentDay) % 2 == 0)
    {
      tryToBuy(price)
    }
    else {
      tryToSell(price)
    }
  }

  private def tryToSell(price: Double): Boolean = {
    if(stocks == 0)
    {
      //println("DO NOTHING")
      return false
    }
    sell(price, stocks)
    true
  }

  private def tryToBuy(price: Double): Boolean = {
    val quantityToBuy = (cash/price).toInt
    if(quantityToBuy == 0) {
     // println("DO NOTHING")
      return false
    }

    buy(price, quantityToBuy)
    //println("BUY for: " + price)
    true
  }
}
