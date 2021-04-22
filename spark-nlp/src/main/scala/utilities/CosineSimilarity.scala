package text.similarity
package utilities

import org.apache.spark.ml.linalg.Vector
import org.apache.spark.ml.linalg.Vectors.norm

object CosineSimilarity {

  /**
   * Element-wise multiplication of corresponding indices
   * of vectors of same length, divided by their norm
   */
  val cosineSimilarity = (x: Vector, y: Vector) => {

    val denom = norm(x, 2) * norm(y, 2)

    var currIdx = 0
    val limit = x.size

    var sum = 0.0

    while (currIdx < limit) {
      sum += x(currIdx) * y(currIdx)
      currIdx += 1
    }

    if (denom == 0.0)
      -1.0
    else
      sum / denom
  }

}
