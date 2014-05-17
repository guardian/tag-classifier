package com.theguardian.tagclassifier

import com.amazonaws.util.StringInputStream
import de.bwaldvogel.liblinear.{Linear, Model}
import java.io.{InputStreamReader, StringWriter, CharArrayWriter}
import scala.util.Try

package object data {
  implicit class RichModel(model: Model) {
    def toCharArray = {
      val writer = new CharArrayWriter()
      Linear.saveModel(writer, model)
      writer.flush()
      val bytes = writer.toCharArray
      writer.close()
      bytes
    }

    def serializeToString = {
      val writer = new StringWriter()
      Linear.saveModel(writer, model)
      writer.flush()
      val string = writer.toString
      writer.close()
      string
    }
  }

  def modelFromString(serialized: String) = Try {
    val stringInputReader = new InputStreamReader(new StringInputStream(serialized))
    Linear.loadModel(stringInputReader)
  }
}
