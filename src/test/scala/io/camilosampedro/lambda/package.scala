package io.camilosampedro

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}

package object lambda {

  val MockedContext: Context = new Context {
    override def getFunctionName: String = ???

    override def getRemainingTimeInMillis: Int = ???

    override def getLogger: LambdaLogger = (string: String) => println(string)

    override def getFunctionVersion: String = ???

    override def getMemoryLimitInMB: Int = ???

    override def getClientContext: ClientContext = ???

    override def getLogStreamName: String = ???

    override def getInvokedFunctionArn: String = ???

    override def getIdentity: CognitoIdentity = ???

    override def getLogGroupName: String = ???

    override def getAwsRequestId: String = ???
  }

}
