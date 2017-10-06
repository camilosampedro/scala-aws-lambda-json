# scala-aws-lambda-json library
[![Build Status](https://travis-ci.org/camilosampedro/scala-aws-lambda-json.svg?branch=master)](https://travis-ci.org/camilosampedro/scala-aws-lambda-json)

You can use this library to get the requests from Lambda as your case classes. You just need to implement `io.github.camilosampedro.lambda.RequestHandler[I, O]`.

```scala
case class MyInput(field: String, field2: Int, field3: Boolean)
case class MyOutput(my: Double, response: String)

import io.camilosampedro.lambda.runtime.JsonRequestHandler
import _root_.com.amazonaws.services.lambda.runtime.Context

class MyHandler extends JsonRequestHandler[MyInput, MyOutput] {
  override def handleRequest(input: MyInput, context: Context): MyOutput = {
    println(s"Your input was: $input")
    MyOutput(2.5, "")
  }
}
```

This will receive Json like

```json
{
  "field": "Fus Roh Dah",
  "field2": 2,
  "field3": false
}
```

And produce the hardcoded output

```json
{
  "my": 1.5,
  "response": "It's no use crying over spilt milk -- it only makes it salty for the cat."
}
```

Then export it as fat-jar

```bash
sbt assembly
```

And upload it to lambda with `MyHandler` as a Handler.