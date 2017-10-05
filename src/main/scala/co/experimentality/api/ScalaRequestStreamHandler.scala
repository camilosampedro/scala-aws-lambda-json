package co.experimentality.api

import java.io.{InputStream, OutputStream}

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import io.circe.parser.decode

trait ScalaRequestStreamHandler[I, O] extends RequestStreamHandler {
  private def extractString(is: InputStream): String = scala.io.Source.fromInputStream(is).mkString

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val rawString = extractString(input)
    decode[I](rawString) match {
      case Right(info) =>
        val result = handleRequest(info, context)
        output.write(result.asJson.noSpaces.toCharArray.map(_.toByte))
        closeOutput(output)
      case Left(error) =>
        context.getLogger.log(s"Error parsing input information: $error")
    }
  }

  private def closeOutput(outputStream: OutputStream): Unit = {
    outputStream.flush()
    outputStream.close()
  }

  def handleRequest(input: I, context: Context): O
}
