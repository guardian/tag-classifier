package controllers

import play.api.mvc.{Controller, Action}
import com.theguardian.tagclassifier.Document
import data.Classifiers
import play.api.libs.json.Json
import models.JsonImplicits._
import models.TagSuggestions

object ClassifierController extends Controller {
  val MaxSuggestions = 10

  def classify = Action(parse.text) { request =>
    val features = Document.extractFeatures(request.body)
    Ok(Json.toJson(TagSuggestions(Classifiers.classify(features).sortBy(-_.probability).take(MaxSuggestions))))
  }

  def index = Action {
    Ok(views.html.index())
  }
}
