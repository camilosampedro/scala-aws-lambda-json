package io.camilosampedro.lambda
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.semiauto._

@JsonCodec case class JsonSample(fieldNameA: String, fieldNameB: Int, fieldNameC: Boolean)
