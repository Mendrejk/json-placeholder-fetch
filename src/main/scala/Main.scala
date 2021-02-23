import ujson.Value.Value

object Main {
  // I'm using lihaoyi's requests and ujson libraries to handle
  // http requests and JSON serialization, as scala lacks a standard way
  // to deal with those.
  def main(args: Array[String]): Unit = {
    val url: String = "https://jsonplaceholder.typicode.com/posts"

    val dataOption: Option[List[Value]] = JsonParse.getJsonValueList(url)
    dataOption match {
      case Some(data) => JsonParse.generateFiles(data)
      case _ => println("Failed fetching data... Please check the url.")
    }
  }
}
