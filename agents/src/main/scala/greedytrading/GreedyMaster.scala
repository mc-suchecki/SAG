package greedytrading

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool
import common.{Init, InitingAgents}

/** Class representing object which initializes all greedy traders. */
class GreedyMaster(nrOfGreedyTraders: Int) extends Actor with InitingAgents  {
  val agentsRouter = context.actorOf(
    Props[GreedyTrader].withRouter(RoundRobinPool(nrOfGreedyTraders)), name = "greedyTradersRouter")

  override def receive: Receive = {
    case Init =>
      initAgents(nrOfGreedyTraders, agentsRouter)
  }
}

