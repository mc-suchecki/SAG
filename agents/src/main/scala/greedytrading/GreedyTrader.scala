package greedytrading

import java.util.concurrent.TimeUnit

import common.{Trade, AbstractTrader, AgentMessage, Init}

import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

/**
 * Class representing greedy trader. It doesn't communicate with other agents. The only information it uses for
 * buy/sell decision making is stock price history.
 */
class GreedyTrader extends AbstractTrader {
  import context._

  val HISTORY_WINDOW_SIZE = 4
  val randomNumberGenerator = new Random()

  var sellThreshold: Double = _
  var history: mutable.ArrayBuffer[Double] = new mutable.ArrayBuffer[Double](HISTORY_WINDOW_SIZE)
  var historyPricesSum: Double = 0

  override def receive: Receive = {
    case Init =>
      register()
      init()

      val initialDelay = new FiniteDuration(0, TimeUnit.SECONDS)
      val interval = new FiniteDuration(1, TimeUnit.SECONDS)

      context.system.scheduler.schedule(initialDelay, interval, self, Trade)

    case Trade =>
      val prevCurrDay = currentDay
      updateCurrentDay()
      if(prevCurrDay != currentDay)
      {
        updateState()
        updateHistory()

        println("total money: " + (cash+history.last*stocks).toString + " " + this.toString)

        trade()
      }
      else
      {
        //println("Still the same day")
      }
  }

  def init(): Unit = {
    val history: Seq[Double] = downloadHistory()
    this.history ++= history.takeRight(HISTORY_WINDOW_SIZE)
    for(value <- history) {
      historyPricesSum += value
    }
  }

  def updateHistory(): Unit = {
    val price = downloadPrice(currentDay-1)
    this.historyPricesSum += price - this.history.head
    this.history.trimStart(1)
    this.history += price
  }

  def averageOfPrices = {
    historyPricesSum/HISTORY_WINDOW_SIZE
  }

  private def trade(): Unit = {
    val price = history.last
    val delta = randomNumberGenerator.nextFloat() * price * 0.1

    if (shouldBuy()) {
      val quantityToBuy = calcQuantityToBuy(price)
      buy(price+delta, quantityToBuy)
      //println("Greedy: BUY")
    }
    else {
      val quantityToSell = (0.5 * stocks).toInt
      sell(price-delta, quantityToSell)
      //println("Greedy: SELL")
    }
  }

  private def shouldBuy(): Boolean = {
    history.last > history.head
  }
  private def shouldSell(): Boolean = {
    history.last > history(HISTORY_WINDOW_SIZE-2)
  }

  private def calcQuantityToBuy(price: Double): Int = {
    (0.5 * cash / price).toInt
  }
}


