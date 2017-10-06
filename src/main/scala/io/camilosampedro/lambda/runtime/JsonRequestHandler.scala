package io.camilosampedro.lambda.runtime

import java.io.{InputStream, OutputStream}

import io.circe.generic.auto._
import io.circe.syntax._
import io.camilosampedro.lambda.model.ErrorMessage
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import io.circe.parser.decode
import JsonRequestHandler._
import io.circe.{Decoder, Encoder}

/**
  * Scala RequestHandler.
  *
  * Implement this to handle a lambda request.
  *
  * @tparam I Input type
  * @tparam O Output type
  */
abstract class JsonRequestHandler[I, O](implicit decoder: Decoder[I], encoder: Encoder[O]) extends RequestStreamHandler {

  /**
    * Handles a request with I JSON format and uses a user defined flow to produce a O JSON object.
    *
    * @param input   Input object
    * @param context Amazon execution helpers
    * @return An object that will be converted to JSON
    */
  def handleRequest(input: I, context: Context): O

  /**
    * Handles a raw request.
    *
    * @param input   Raw input from the client
    * @param output  Raw output to the client
    * @param context Amazon execution helpers
    */
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    // Get the String from the raw InputStream
    val rawString = extractString(input)
    // Decode the JSON content and put it into the I class
    val resultMessage = decode[I](rawString)(decoder) match {
      case Right(json) =>
        // If there is indeed a json with this format, perform the request handling
        val result = handleRequest(json, context)
        // The result message will be the response
        Right(result)
      case Left(error) =>
        // If there is not a json with this format, raise an error and don't execute anything
        context.getLogger.log(s"[Error] error parsing input information: $error")
        // The result message will be the error
        Left(ErrorMessage(s"Error parsing input information: $error"))
    }
    // Convert the result to a byte array, the format required by OutputStream
    val byteArray = convertToByteArray(resultMessage)
    // Write the result into the output
    output.write(byteArray)
    // Clean the output before exiting
    closeOutput(output)
  }

  /**
    * Convert the result message into an byte array.
    *
    * @param resultMessage Result message with an error or user output.
    * @return Byte array of the resultMessage
    */
  private def convertToByteArray(resultMessage: Either[ErrorMessage, O]): Array[Byte] = {
    val json = resultMessage match {
      case Left(l) => l.asJson
      case Right(r) => r.asJson(encoder)
    }
    json.noSpaces.toCharArray.map(_.toByte)
  }
}

object JsonRequestHandler {
  private def extractString(is: InputStream): String = scala.io.Source.fromInputStream(is).mkString

  private def closeOutput(outputStream: OutputStream): Unit = {
    outputStream.flush()
    outputStream.close()
  }
}