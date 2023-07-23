import java.awt.Graphics2D
import scala.io.Source
import scala.util.Using
import io.circe.generic.auto._
import io.circe.jawn.decode
import io.circe.syntax._

import scala.collection.mutable.Buffer

object Simulation {

  val planetData = Using(Source.fromFile("src/main/data/astronomicalBodyData.json")) {
    source => decode[Array[AstronomicalBody]](source.getLines().mkString)
  }.toEither.flatten

  val astronomicalBodies = Buffer[AstronomicalBody]()

  for (array <- planetData) {
   for (astronomicalBody <- array) {
     astronomicalBodies += astronomicalBody
   }
  }

  def step() = {
    astronomicalBodies.foreach(_.move())
  }

  def checkCollision = {
    astronomicalBodies.exists(_.hasCollided)
  }

  def draw(g: Graphics2D): Unit = {
    astronomicalBodies.foreach(_.draw(g))
  }

}
