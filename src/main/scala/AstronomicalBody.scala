/*
This class is used to create the astronomical bodies that
appear in the simulation.
*/

import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.Color
import scala.collection.mutable
import scala.math.{atan2, cos, pow, sin, sqrt}


case class AstronomicalBody(name: String, radius: Double, mass: Double, var velocity: Vector, var position: Vector) {

  val au = 1.49597871E11          // astronomical unit as meters
  val g = 6.67384 * pow(10, -11)  // gravitational constant
  val scale = 250 / au            // distances to sun are scaled so that one astronomical unit is 250 pixels (radiuses aren't in scale)


  /*
  Circles that represent astronomical bodies in simulation
   */
  var circle = new Ellipse2D.Double(position.x * scale + GUI.width / 2.0 - radius / 2.0 - 200, position.y * scale + GUI.height / 2.0 - radius / 2.0, radius, radius)

  /*
  Every astronomical body is assigned with unique color
   */
  val colors = Array[Color](Color.yellow, Color.gray, new Color(255, 140, 0), new Color(0, 191, 255), Color.red, Color.white)
  val color = name match {
    case "Sun"       => colors(0)
    case "Mercury"   => colors(1)
    case "Venus"     => colors(2)
    case "Earth"     => colors(3)
    case "Mars"      => colors(4)
    case "Satellite" => colors(5)
  }

/*
X and y components of the gravitational force between two astronomical bodies
 */
  def forceBetween(other: AstronomicalBody) = {
    val dist = this.position.distanceTo(other.position)
    val distx = other.position.x - this.position.x
    val disty = other.position.y - this.position.y
    val force = g * this.mass * other.mass / pow(dist, 2)
    val angle = atan2(disty, distx)
    val forcex = cos(angle) * force
    val forcey = sin(angle) * force
    (forcex, forcey)
  }

  def touches(other: AstronomicalBody): Boolean = {
    val thisPos = Vector(this.position.x * scale + GUI.width / 2.0 - radius / 2.0, this.position.y * scale + GUI.width / 2.0 - radius / 2.0)
    val otherPos = Vector(other.position.x * scale + GUI.width / 2.0 - radius / 2.0, other.position.y * scale + GUI.width / 2.0 - radius / 2.0)
    thisPos.distanceTo(otherPos) < this.radius / 2.0 + other.radius / 2.0
  }

  def hasCollided = {
    val astronomicalBodies = Simulation.astronomicalBodies.filterNot(_ == this)
    val collisionBuffer = mutable.Buffer[Boolean]()
    for (i <- astronomicalBodies) {
      collisionBuffer += i.touches(this)
    }
    !collisionBuffer.forall(_ == false)
  }

  /*
  Updates astronomical bodies positions and velocities
  */
  def move() = {
    var totalForcex = 0.0
    var totalForcey = 0.0
    for (astronomicalBody <- Simulation.astronomicalBodies if (this != astronomicalBody)) {
        val forces = this.forceBetween(astronomicalBody)
        totalForcex = totalForcex + forces._1
        totalForcey = totalForcey + forces._2
    }
    this.velocity.x = this.velocity.x + totalForcex / this.mass * GUI.timestep
    this.velocity.y = this.velocity.y + totalForcey / this.mass * GUI.timestep
    this.position += this.velocity * GUI.timestep
    circle = new Ellipse2D.Double(position.x * scale + GUI.width / 2.0 - radius / 2.0, position.y * scale + GUI.height / 2.0 - radius / 2.0, radius, radius)
  }

  /*
  Draws the astronomical bodies on screen
   */
  def draw(g: Graphics2D) = {
      g.setColor(color)
      g.fill(circle)
  }

}
