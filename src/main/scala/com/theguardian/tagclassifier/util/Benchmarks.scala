package com.theguardian.tagclassifier.util

import objectexplorer.ObjectGraphMeasurer.Footprint

object Benchmarks {
  /** Objects have about 16 bytes of headers */
  val ObjectOverhead = 16

  /** Assuming a 64-bit architecture */
  val ReferenceSize = 8
  val IntSize = 4
  val CharSize = 2

  implicit class RichFootprint(footprint: Footprint) {
    /** Estimate because in reality objects have padding, etc. to bring them up to the word size */
    def estimatedSizeInBytes = {
      footprint.getObjects * ObjectOverhead +
        footprint.getReferences * ReferenceSize +
        integers * IntSize +
        chars * CharSize
    }

    def integers = footprint.getPrimitives.count(Integer.TYPE)

    def chars = footprint.getPrimitives.count(Character.TYPE)
  }

}