package common

import akka.actor.{ActorRef, Actor}

/** Super-class for all master-agents. */
abstract class AbstractMaster extends Actor {
  def initAgents(nrOfTraders: Int, agentsRouter: ActorRef): Unit ={
    for (i <- 0 until nrOfTraders)
    {
      agentsRouter ! Init
    }
  }
}
