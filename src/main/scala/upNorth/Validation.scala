package upNorth

import cats.implicits._
import cats.data.ValidatedNel

sealed trait Error
case object Empty extends Error
case class ContainsSwear(swear: String, item: String) extends Error

object Validation {

  private def validateEmpty(item: String): ValidatedNel[Error, String] = {
    if (item.isEmpty) Empty.invalidNel
    else item.valid
  }

  private def validateSwear(item: String): ValidatedNel[Error, String] = {
    if (item.contains("darn")) ContainsSwear("darn", item).invalidNel
    else item.valid
  }

  def validateItem(item: String): ValidatedNel[Error, String] =
    (validateEmpty(item) |@| validateSwear(item)).tupled.map(_._1)

}
