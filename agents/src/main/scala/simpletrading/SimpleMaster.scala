package simpletrading

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool
import common.{AbstractMaster, Init}

/** Class representing object which initializes all simple agents. */
class SimpleMaster(nrOfSimpleTraders: Int) extends AbstractMaster {
    val agentsRouter = context.actorOf(
      Props[SimpleTrader].withRouter(RoundRobinPool(nrOfSimpleTraders)), name = "simpleAgentsRouter")

    override def receive: Receive = {
      case Init =>
        initAgents(nrOfSimpleTraders, agentsRouter)
    }
}

