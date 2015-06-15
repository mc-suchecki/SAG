package common

import akka.actor.Actor
import com.google.gson.JsonParser
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient

/** Super-class for all trading agents. */
abstract class AbstractTrader extends Actor {
  private val url = "http://localhost:5000/traders"

  protected var httpClient = new DefaultHttpClient

  protected var id: Integer = null
  protected var cash: Integer = null
  protected var stocks: Integer = null

  def register() = {
    val post = new HttpPost(url)

    val response = httpClient.execute(post)
    val entity = response.getEntity
    var content = ""
    
    if(entity == null)
    {
      throw new ResponseException
    }

    val inputStream = entity.getContent
    content = io.Source.fromInputStream(inputStream).getLines().mkString
    inputStream.close()

    val jsonParser = new JsonParser()
    val jsonObject = jsonParser.parse(content).getAsJsonObject

    cash = jsonObject.get("cash").getAsInt
    id = jsonObject.get("id").getAsInt
    stocks = jsonObject.get("stocks").getAsInt

    httpClient.getConnectionManager.shutdown()

    println(this.toString)
  }

  override def toString: String = {
    "cash:" + cash + ", id:" + id + ", stocks:" + stocks
  }
}

class ResponseException extends Exception {
}
