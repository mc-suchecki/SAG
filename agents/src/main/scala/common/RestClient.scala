package common

import com.google.gson.{JsonObject, JsonParser}
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.DefaultHttpClient

/**
 * TODO
 */
trait RestClient {
  protected var httpClient = new DefaultHttpClient

  protected def send(request: HttpUriRequest): JsonObject = {
    val response = httpClient.execute(request)

    val entity = response.getEntity
    var content = ""

    if(entity == null)
    {
      throw new ResponseException
    }

    try {
      val inputStream = entity.getContent
      content = io.Source.fromInputStream(inputStream).getLines().mkString
      inputStream.close()

      if(content.isEmpty)
      {
        return null
      }

      val jsonParser = new JsonParser()
      jsonParser.parse(content).getAsJsonObject
    }
    catch
    {
      case e:Exception =>
        println(content)
        throw e;
    }
  }
}
