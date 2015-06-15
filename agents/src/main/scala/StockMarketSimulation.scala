import akka.actor.{ActorSystem, Props}
import common.Init
import grouptrading.GroupMaster
import simpletrading.SimpleMaster

object StockMarketSimulation {
  def main(args: Array[String]) {
    if(args.length != 2)
    {
      println("Usage: trade nrOfGroupTraders nrOfSimpleTraders")
      return;
    }

    val nrOfGroupTraders = args(0).toInt
    val nrOfSimpleTraders = args(0).toInt

    val system = ActorSystem("StockMarket")

    val groupMaster = system.actorOf(Props(new GroupMaster(nrOfGroupTraders)))
    val simpleMaster = system.actorOf(Props(new SimpleMaster(nrOfSimpleTraders)))
    simpleMaster ! Init
  }
}





