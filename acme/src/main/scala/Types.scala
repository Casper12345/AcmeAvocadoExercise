package acme.types

import scala.util.Try

object Imports {

  trait FromRecord[A] {
    def apply(record: Record): Option[A]
  }

  trait ToRecord[A] {
    def apply(a: A): Record
  }

  type Record = Vector[String]

  implicit class FromRecordSyntax(r: Record) {
    def fromRecord[A](implicit A: FromRecord[A]): Option[A] = A(r)
  }

  implicit class ToRecordSyntax[A](a: A) {
    def toRecord(implicit A: ToRecord[A]): Record = A(a)
  }

  case class AcmeCustomerAttributes(
    id: Int,
    firstName: String,
    lastName: String,
    age: Int,
    ltvEst: Int,
    churnScore: Double,
    segmentId: Int
  )

  object AcmeCustomerAttributes {
    implicit val attrsFromRecord: FromRecord[AcmeCustomerAttributes] =
      new FromRecord[AcmeCustomerAttributes] {
        def apply(r: Record) = for {
          id         <- Try(r(0).toInt).toOption
          firstName  <- Try(r(1)).toOption
          lastName   <- Try(r(2)).toOption
          age        <- Try(r(3).toInt).toOption
          ltvEst     <- Try(r(4).toInt).toOption
          churnScore <- Try(r(5).toDouble).toOption
          segmentId  <- Try(r(6).toInt).toOption
        } yield AcmeCustomerAttributes(
                  id, firstName, lastName,
                  age, ltvEst, churnScore, segmentId)
      }

    implicit val attrsToRecord: ToRecord[AcmeCustomerAttributes] =
      new ToRecord[AcmeCustomerAttributes] {
        def apply(a: AcmeCustomerAttributes) =
          Vector(a.id.toString,
                 a.firstName.toString,
                 a.lastName.toString,
                 a.age.toString,
                 a.ltvEst.toString,
                 a.churnScore.toString,
                 a.segmentId.toString)
      }
  }

  case class AcmeSegmentInfo(id: Int, count: Int)

  object AcmeSegmentInfo {
    implicit val segmentInfoFromRecord: FromRecord[AcmeSegmentInfo] =
      new FromRecord[AcmeSegmentInfo] {
        def apply(r: Record) = for {
          id <- Try(r(0).toInt).toOption
          count <- Try(r(1).toInt).toOption
        } yield AcmeSegmentInfo(id, count)
      }

    implicit val segmentInfoToRecord: ToRecord[AcmeSegmentInfo] =
      new ToRecord[AcmeSegmentInfo] {
        def apply(a: AcmeSegmentInfo) =
          Vector(a.id.toString, a.count.toString)
      }
  }

}

