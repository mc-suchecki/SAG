import akka.actor.{ActorSystem, Props}
import common.Init
import config.Configuration
import greedytrading.GreedyMaster
import grouptrading.GroupMaster
import simpletrading.SimpleMaster

object StockMarketSimulation {
  def main(args: Array[String]) {

    val system = ActorSystem("StockMarket")

    val nrOfGroupTraders = Configuration.nrOfGroupTraders
    val nrOfSimpleTraders = Configuration.nrOfSimpleTraders
    val nrOfGreedyTraders = Configuration.nrOfGreedyTraders

    val groupMaster = system.actorOf(Props(new GroupMaster(nrOfGroupTraders)))
    val simpleMaster = system.actorOf(Props(new SimpleMaster(nrOfSimpleTraders)))
    val greedyMaster = system.actorOf(Props(new GreedyMaster(nrOfGreedyTraders)))

    simpleMaster ! Init
    greedyMaster ! Init
  }
}





