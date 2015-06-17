package grouptrading

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, Props}
import akka.routing.RoundRobinPool
import common._

import scala.concurrent.duration.FiniteDuration

/**
 * Master for all group agents. It makes all buy/sell decisions by itself and commands group agents to take proper
 * actions.
 */
class GroupMaster(nrOfGroupTraders: Int) extends AbstractTrader with InitingAgents
{
  import context._

  val agentsRouter = context.actorOf(
    Props[GroupTrader].withRouter(RoundRobinPool(nrOfGroupTraders)), name = "groupAgentsRouter")
  val CYCLE_LENGTH = 4

  var buyingPhase = true
  var buyingIteration = 0
  var sellingIteration = 0

  var lastPrice: Double = _

  override def receive: Receive = {
    case Init =>
      initAgents(nrOfGroupTraders, agentsRouter)

      val initialDelay = new FiniteDuration(0, TimeUnit.SECONDS)
      val interval = new FiniteDuration(5, TimeUnit.SECONDS)

      context.system.scheduler.schedule(initialDelay, interval, self, Trade)

    case Trade =>
      val prevCurrDay = currentDay
      updateCurrentDay()
      if(prevCurrDay != currentDay)
      {
        updateState()
        lastPrice = downloadPrice(currentDay-1)
        trade()
      }
      else
      {
        //println("Still the same day")
      }
  }

  private def trade(): Unit = {
    if(buyingPhase)
    {
      if(buyingIteration >= CYCLE_LENGTH) {
        buyingPhase = false
        sellingIteration = 0
        sellEverything()
      }
      else {
        pumpTheBubble()
      }
    }
    else {
      if(sellingIteration >= CYCLE_LENGTH) {
        buyingPhase = true
        buyingIteration = 0
        pumpTheBubble()
      }
      else {
        sellEverything()
      }
    }
  }

  private def pumpTheBubble(): Unit = {
    if(buyingIteration < CYCLE_LENGTH/2) {
      broadcast(new Buy(0.5, 1.04 * lastPrice))
    }

    buyingIteration += 1
  }

  private def sellEverything(): Unit = {
    sellingIteration += 1
    broadcast(new Sell(0.8, lastPrice))
  }

  private def broadcast(message: GroupAgentMessage): Unit = {
    for (i <- 0 until nrOfGroupTraders)
    {
      agentsRouter ! message
    }
  }

}
