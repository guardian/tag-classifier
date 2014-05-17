package models

import play.api.libs.json.Json

object JsonImplicits {
  implicit val suggestionWrites = Json.writes[Suggestion]
  implicit val tagSuggestionsWrites = Json.writes[TagSuggestions]
}
