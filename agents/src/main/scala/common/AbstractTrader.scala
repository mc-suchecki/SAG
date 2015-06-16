package common

import java.util

import akka.actor.Actor
import com.google.gson.JsonArray
import config.Configuration
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.message.BasicNameValuePair

import scala.collection.JavaConversions._
import scala.collection.mutable

/** Super-class for all trading agents. */
abstract class AbstractTrader extends Actor with RestClient {
  protected var id: Integer = null
  protected var cash: Integer = null
  protected var stocks: Integer = null
  protected var currentDay: Integer = null

  protected def register() = {
    val postRequest = new HttpPost(Configuration.serverUrl + "/traders")
    val jsonObject = send(postRequest)

    cash = jsonObject.get("cash").getAsInt
    id = jsonObject.get("id").getAsInt
    stocks = jsonObject.get("stocks").getAsInt
  }

  protected def downloadHistory(): mutable.Seq[Double] = {
    val getRequest = new HttpGet(Configuration.serverUrl + "/stock/history")
    val jsonObject = send(getRequest)
    val jsonArray: JsonArray = jsonObject.getAsJsonArray("price_history")
    val history = new mutable.MutableList[Double]

    for(jsonElement <- jsonArray)
      history += jsonElement.getAsDouble

    history
  }

  protected def downloadPrice(day: Int): Double = {
    val requestUrl: String = Configuration.serverUrl + "/stock/price?day=" + day.toString
    println(requestUrl)
    val getRequest = new HttpGet(requestUrl)
    val jsonObject = send(getRequest)

    jsonObject.getAsJsonPrimitive("price").getAsDouble
  }

  protected def downloadCurrentDay(): Int = {
    val getRequest = new HttpGet(Configuration.serverUrl + "/day")
    val jsonObject = send(getRequest)
    jsonObject.getAsJsonPrimitive("day").getAsInt
  }

  protected def updateState(): Unit = {
    val getRequest = new HttpGet(Configuration.serverUrl + "/traders/" + this.id.toString)
    val jsonObject = send(getRequest)

    cash = jsonObject.get("cash").getAsInt
    id = jsonObject.get("id").getAsInt
    stocks = jsonObject.get("stocks").getAsInt
  }

  protected def buy(price:Double, stocks:Int): Unit = {
    //price stocks trader_id
    val postRequest = new HttpPost(Configuration.serverUrl + "/stock/buy")

    val parameters = new util.ArrayList[NameValuePair](3)
    parameters.add(new BasicNameValuePair("price", price.toString))
    parameters.add(new BasicNameValuePair("stocks", stocks.toString))
    parameters.add(new BasicNameValuePair("trader_id", id.toString))
    postRequest.setEntity(new UrlEncodedFormEntity(parameters))

    send(postRequest)
  }

  protected def sell(price:Double, stocks:Int): Unit = {
    val postRequest = new HttpPost(Configuration.serverUrl + "/stock/sell")

    val parameters = new util.ArrayList[NameValuePair](3)
    parameters.add(new BasicNameValuePair("price", price.toString))
    parameters.add(new BasicNameValuePair("stocks", stocks.toString))
    parameters.add(new BasicNameValuePair("trader_id", id.toString))
    postRequest.setEntity(new UrlEncodedFormEntity(parameters))

    send(postRequest)
  }

  override def toString: String = {
    "cash:" + cash + ", id:" + id + ", stocks:" + stocks
  }
}

class ResponseException extends Exception {
}
