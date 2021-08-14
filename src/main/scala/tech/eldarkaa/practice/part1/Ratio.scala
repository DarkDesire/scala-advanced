package tech.eldarkaa.practice.part1

final case class Ratio(num: Int, den: Int)

object Ratio {
  def make(num: Int, den: Int): Ratio = {
    val gcd = BigInt(num).gcd(den).toInt
    Ratio(num / gcd, den / gcd)
  }
  implicit val ord: Ord[Ratio] = (x: Ratio, y: Ratio) => Ord.compareFromInt(x.num * y.den - y.num * x.den)
}
