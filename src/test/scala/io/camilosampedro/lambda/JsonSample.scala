package io.camilosampedro.lambda
import io.circe.generic.JsonCodec

@JsonCodec case class JsonSample(fieldNameA: String, fieldNameB: Int, fieldNameC: Boolean)
