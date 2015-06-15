package common

import akka.actor.Actor
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient

/** Super-class for all trading agents. */
abstract class AbstractTrader extends Actor {
  def register() = {
    val url = "http://localhost:5000/traders"
    val client  = new DefaultHttpClient
    val post = new HttpPost(url)

    val response = client.execute(post)
    val entity = response.getEntity()
    var content = ""
    if(entity != null)
    {
      val inputStream = entity.getContent()
      content = io.Source.fromInputStream(inputStream).getLines().mkString
      inputStream.close()
      println(content)
    }
    client.getConnectionManager().shutdown()
  }
}
