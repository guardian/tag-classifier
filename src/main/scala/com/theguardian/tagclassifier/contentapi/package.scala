package com.theguardian.tagclassifier

import com.gu.openplatform.contentapi.model.{SearchResponse, Content}

package object contentapi {

  implicit class RichContent(content: Content) {
    def body = content.safeFields.get("body")
  }

  implicit class RichSearchResponse(searchResponse: SearchResponse) {
    def isLastPageOrBeyond = searchResponse.currentPage >= searchResponse.pages
  }
}
