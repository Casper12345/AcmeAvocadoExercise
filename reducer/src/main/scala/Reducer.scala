package acme.reducer

import scala.util.Try
import java.io._

object Main {

  import acme.types.Imports.AcmeCustomerAttributes._
  import acme.types.Imports._
  import acme.EMR

  def main(args: Array[String]): Unit = {

    val grouped: Iterable[(String, Vector[AcmeCustomerAttributes])] = EMR.group(
      EMR.lines.flatMap(str =>
        EMR.decodeKeyed[String, AcmeCustomerAttributes](
          v => Try(v.split(EMR.keyDelimiter).head).toOption,
          k => Try(k.split(EMR.keyDelimiter).tail.reduce((a, b) => a + EMR.keyDelimiter + b))
            .toOption.flatMap(s => EMR.decodeDelimited(EMR.keyDelimiter, s)),
          str,
          str
        )
      )
    )

    def writeToFile(pw: PrintWriter, s: String): PrintWriter = {
      pw.write(s)
      pw
    }

    val pw = writeToFile(new PrintWriter(new File("out.tsv")), s"SegmentId${EMR.keyDelimiter}Count\n")

    grouped.map { case (segmentId, group) =>
      writeToFile(pw, s"$segmentId${EMR.keyDelimiter}${group.size}\n")
    }.lastOption.foreach(_.close())

  }


}
