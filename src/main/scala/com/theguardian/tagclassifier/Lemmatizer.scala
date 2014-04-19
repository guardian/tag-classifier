package com.theguardian.tagclassifier

import collection.JavaConversions._
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import java.util.Properties
import edu.stanford.nlp.ling.CoreAnnotations.{LemmaAnnotation, TokensAnnotation, SentencesAnnotation}

object Lemmatizer {
  private val props = new Properties()
  props.put("annotators", "tokenize, ssplit, pos, lemma")

  private val pipeline = new StanfordCoreNLP(props)

  def lemmatize(text: String) = {
    val startTime = System.currentTimeMillis
    def elapsed = System.currentTimeMillis - startTime
    val document = new Annotation(text)
    pipeline.annotate(document)

    val sentences = document.get(classOf[SentencesAnnotation])

    val lemmas = sentences flatMap { sentence =>
      val tokens = sentence.get(classOf[TokensAnnotation])

      tokens map { _.get(classOf[LemmaAnnotation]) }
    }

    println(s"Lemmatized text with ${text.length} words in ${elapsed}ms")

    lemmas.toList.filterNot(_.forall(!_.isLetterOrDigit))
  }
}
