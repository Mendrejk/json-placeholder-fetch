import java.io.{BufferedWriter, File, FileWriter}
import java.net.MalformedURLException

import requests.Response
import ujson.Value.Value

object JsonParse {
  // a function fetching and returning a JSON Value object from the given URL
  // opted to use option instead of throwing exceptions, as I find it
  // easier to work with, and less error-prone
  def getJsonValue(url: String): Option[Value] = {
    try {
      // fetching the request from the given url via the requests library
      val response: Response = requests.get(url)
      // parsing the response into JSON via the ujson library
      Some(ujson.read(response.text()))
    } catch {
      case e: MalformedURLException => None
      case e: Exception => None
    }
  }

  // similar to getJsonValue, but basically flattens the JSON provided it's nested,
  // returning an immutable list of Values
  def getJsonValueList(url: String): Option[List[Value]] = {
    getJsonValue(url) match {
      case Some(value) => value.arrOpt match {
        case Some(values) => Some(values.toList)
        case None => None
      }
      case None => None
    }
  }

  // creates and fills files with individual JSON Value data
  def generateFiles(data: List[Value]): Unit = {
    data.foreach(value => {
      // using Java's BufferedWriter to access the files
      val fileName: String = value("id").toString()
      val file = new File(s"$fileName.json")
      val writer = new BufferedWriter(new FileWriter(file))
      // ujson's writeTo function is used to easily save JSON to given file.
      // allows to specify the indent value, improving readability.
      ujson.writeTo(value, writer, 4)
      writer.close()
    })
  }
}
