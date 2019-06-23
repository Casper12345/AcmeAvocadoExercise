package acme.mapper

import acme.types.Imports._
import acme.EMR

object Main {

  import acme.types.Imports.AcmeCustomerAttributes._

  def main(args: Array[String]): Unit = {

    EMR.interactIO { s =>
      EMR.decodeDelimited(EMR.keyDelimiter, s).filter(a => a.age > 17 && a.age < 33 && a.churnScore >= 0.95)
        .map(a =>
          EMR.encodeKeyed[AcmeCustomerAttributes](
            _ => a.segmentId.toString,
            b => EMR.encodeDelimited(EMR.keyDelimiter, b), a)
        )
    }
  }

}

