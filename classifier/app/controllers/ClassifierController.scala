package controllers

import play.api.mvc.{Controller, Action}
import com.theguardian.tagclassifier.Document
import data.Classifiers
import models.{TagSuggestions, Suggestion}
import play.api.libs.json.Json
import models.JsonImplicits._

object ClassifierController extends Controller {
  val MaxSuggestions = 10

  def classify = Action(parse.text) { request =>
    val features = Document.extractFeatures(request.body)

    val classifiers = Classifiers.all.get()

    Ok(Json.toJson(TagSuggestions((classifiers.mapValues(_.classify(features)).toList map { case (tagId, probability) =>
      Suggestion(tagId, probability)
    }).sortBy(-_.probability).take(MaxSuggestions))))
  }
}
