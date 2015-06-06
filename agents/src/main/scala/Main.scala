import akka.actor.Actor
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient

sealed trait GroupAgentMessage
case object Trade extends GroupAgentMessage
case class Init(serverAddress: String) extends GroupAgentMessage
case class Buy() extends GroupAgentMessage
case class Sell() extends GroupAgentMessage

class SimpleTrader extends Actor {

  def init(serverAddress: String): Unit = {
    val url = "http://192.168.2.1:5000"
    val client  = new DefaultHttpClient
    val post = new HttpPost(url)

    val response = client.execute(post)


  }

  override protected def receive: Receive = {
    case Init(serverAddress) => init(serverAddress)
  }
}
