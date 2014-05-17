package models

case class Suggestion(
  tagId: String,
  probability: Double
)

case class TagSuggestions(
  suggestions: List[Suggestion]
)