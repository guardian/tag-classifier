import scala.collection.parallel.ParSeq
import scalaz._
import scalaz.PLens._

package object miner {
  def parSeqHeadPLens[A]: ParSeq[A] @?> A = plens { xs =>
    for {
      h <- xs.headOption
    } yield Store(_ +: xs.tail, h)
  }
}
