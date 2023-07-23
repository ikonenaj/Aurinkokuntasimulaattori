/* Class that is used to set the properties of the
// astronomical bodies. Vectors are used to indicate
// the positions, velocities and accelerations of
// all the different bodies.
*/

import scala.math.{sqrt, pow}

case class Vector(var x: Double, var y: Double) {

  def distanceTo (other: Vector): Double = {
    val distVector = Vector(this.x - other.x, this.y - other.y)
    sqrt(pow(distVector.x, 2) + pow(distVector.y, 2))
  }

  def +(other: Vector) = {
    Vector(this.x + other.x, this.y + other.y)
  }

  def *(mul: Double) : Vector = {
    Vector(this.x * mul, this.y * mul)
  }

}
