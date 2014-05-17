package miner

import com.gu.openplatform.contentapi.model.{SearchResponse, Content}

package object contentapi {
  implicit class RichContent(content: Content) {
    def body = content.safeFields.get("body")

    def bodyOrDie = body getOrElse {
      throw new RuntimeException("Expected Content to have body, but had none. Is your query correct?")
    }
  }

  implicit class RichSearchResponse(searchResponse: SearchResponse) {
    def isLastPageOrBeyond = searchResponse.currentPage >= searchResponse.pages
  }
}
