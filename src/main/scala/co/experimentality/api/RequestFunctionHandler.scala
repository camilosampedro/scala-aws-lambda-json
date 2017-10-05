package co.experimentality.api

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler, RequestStreamHandler}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

class RequestFunctionHandler extends RequestStreamHandler {

  def extractString(is: InputStream): String = scala.io.Source.fromInputStream(is).mkString

  override def handleRequest(is: InputStream, os: OutputStream, context: Context): Unit = {
    val rawString = extractString(is)
    val information = decode[Information](rawString) match {
      case Right(info) => info
      case Left(error) => context.getLogger.log(s"Error parsing information: $error")
    }
    os.write(s"You invoked a lambda function with $information".toCharArray.map(_.toByte))
    os.flush()
    os.close()
  }
}
