package io.camilosampedro.lambda

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.Context
import io.camilosampedro.lambda.runtime.JsonRequestHandler
import org.scalatest.WordSpec
import org.scalatest.exceptions.TestFailedException

class JsonRequestHandlerSpec extends WordSpec {

  import JsonRequestHandlerSpec._

  private val sampleJson =
    """
      |{
      | "fieldNameA": "value1",
      | "fieldNameB": 0,
      | "fieldNameC": true
      |}
    """.stripMargin

  "An JsonSample configured RequestHandler" should {
    val requestHandlerWithString = new JsonRequestHandler[JsonSample, JsonSample] {
      override def handleRequest(input: JsonSample, context: Context): JsonSample = {
        // Return it as it comes
        input.copy(fieldNameA = "changed", fieldNameB = 1, fieldNameC = false)
      }
    }
    "be read successfully by the underground handler" in {
      val inputStream = createInputStream(sampleJson)
      val outputStream = createOutputStream()
      requestHandlerWithString.handleRequest(inputStream, outputStream, MockedContext)
      val result = retrieveContentFromOutputStream(outputStream)
      assert(result == "{\"fieldNameA\":\"changed\",\"fieldNameB\":1,\"fieldNameC\":false}")
    }
  }
}

object JsonRequestHandlerSpec {
  def createInputStream(s: String): InputStream = new ByteArrayInputStream(s.getBytes)

  def createOutputStream(): OutputStream = new ByteArrayOutputStream()

  def retrieveContentFromOutputStream(outputStream: OutputStream): String = {
    outputStream match {
      case o: ByteArrayOutputStream => new String(o.toByteArray.map(_.toChar))
      case _ =>
        println("Error retrieving content from outputStream")
        throw new TestFailedException("Error retrieving content from outputStream", 0)
    }
  }
}
