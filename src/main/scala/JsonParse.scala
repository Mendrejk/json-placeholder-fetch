import java.net.MalformedURLException

import requests.Response
import ujson.Value.Value

object JsonParse {
  def getJsonValue(url: String): Option[Value] = {
    try {
      val response: Response = requests.get(url)
      Some(ujson.read(response.text()))
    } catch {
      case e: MalformedURLException => None
      case e: Exception => None
    }
  }

  def getJsonValueList(url: String): Option[List[Value]] = {
    getJsonValue(url) match {
      case Some(value) => value.arrOpt match {
        case Some(values) => Some(values.toList)
        case None => None
      }
      case None => None
    }
  }
}
