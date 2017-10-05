package co.experimentality.api

import com.amazonaws.services.lambda.runtime.Context

class RequestFunctionHandler extends ScalaRequestStreamHandler[Information, String] {
  override def handleRequest(input: Information, context: Context): String = s"You invoked a lambda function with $input"
}
