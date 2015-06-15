package simpletrading

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool
import common.{AbstractMaster, Init}

/**
 * Created by jacek on 6/15/15.
 */
class SimpleMaster(nrOfSimpleTraders: Int) extends AbstractMaster {
    val agentsRouter = context.actorOf(
      Props[SimpleTrader].withRouter(RoundRobinPool(nrOfSimpleTraders)), name = "simpleAgentsRouter")

    override def receive: Receive = {
      case Init => {
        initAgents(nrOfSimpleTraders, agentsRouter)
      }
    }
}

