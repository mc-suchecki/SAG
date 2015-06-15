package grouptrading

import akka.actor.{ActorRef, Actor, Props}
import akka.routing.RoundRobinPool
import common.{AbstractMaster, Init, AgentMessage}
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient

/**
 * Master for all group agents. It makes all buy/sell decisions by itself and commands group agents to take proper
 * actions.
 */
class GroupMaster(nrOfGroupTraders: Int) extends AbstractMaster
{
  val agentsRouter = context.actorOf(
    Props[GroupTrader].withRouter(RoundRobinPool(nrOfGroupTraders)), name = "groupAgentsRouter")

  override def receive: Receive = {
    case Init =>
      println("GroupMaster INIT")
      initAgents(nrOfGroupTraders, agentsRouter)
  }

  //TODO
}
