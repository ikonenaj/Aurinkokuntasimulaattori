import java.awt.event.ActionListener
import java.awt.{Color, Graphics2D}
import scala.swing._
import scala.swing.event.ButtonClicked
import scala.collection.mutable.HashMap

/* This object is used to create and update the user interface
// as the simulation progresses.
*/

object GUI extends SimpleSwingApplication {

  val width = 800  // window width
  val height = 800  // simulation app height
  val fullHeight = 810  // whole window height
  val au = 1.49597871E11
  var timestep = 3600 * 4
  var simLength = 0

  def top = new MainFrame {

    title = "Solar System Simulator"
    resizable = false

    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)

    val startPrompt = new Label {
        override def paintComponent(g: Graphics2D): Unit = {
          g.setColor(new Color(255, 255, 255))
          g.drawString("Start the simulation without a satellite", 80, height / 2 - 50)
        }
      }

    startPrompt.minimumSize   = new Dimension(width / 2, height / 2)
    startPrompt.preferredSize = new Dimension(width / 2, height / 2)
    startPrompt.maximumSize   = new Dimension(width / 2, height / 2)

    val settingPrompt = new Label {
        override def paintComponent(g: Graphics2D): Unit = {
          g.setColor(new Color(255, 255, 255))
          g.drawString("Start the simulation with a satellite", 60, height / 2 - 50)
        }
      }

    settingPrompt.minimumSize   = new Dimension(width / 2, height / 2)
    settingPrompt.preferredSize = new Dimension(width / 2, height / 2)
    settingPrompt.maximumSize   = new Dimension(width / 2, height / 2)

    val prompts = new BoxPanel(Orientation.Horizontal) {
      override def paintComponent(g: Graphics2D) = {
        g.setColor(new Color(0, 0, 0))
        g.fillRect(0, 0, width, fullHeight)
      }
      contents += startPrompt
      contents += settingPrompt
    }

    val startButton    = new Button("Start")
    val settingsButton = new Button("Set up satellite")

    val emptySpace = new Panel {
      override def paintComponent(g: Graphics2D) = {
        g.setColor(new Color(0, 0, 0))
        g.fillRect(0, 0, width, fullHeight)
      }
      minimumSize   = new Dimension(280, height / 8)
      preferredSize = new Dimension(280, height / 8)
      maximumSize   = new Dimension(280, height / 8)
    }

    val buttons = new BoxPanel(Orientation.Horizontal) {
      override def paintComponent(g: Graphics2D) = {
        g.setColor(new Color(0, 0, 0))
        g.fillRect(0, 0, width, fullHeight)
      }
      contents += startButton
      contents += emptySpace
      contents += settingsButton
    }

    val start = new BoxPanel(Orientation.Vertical) {
      override def paintComponent(g: Graphics2D) = {
        g.setColor(new Color(0, 0, 0))
        g.fillRect(0, 0, width, fullHeight)
      }
      contents += prompts
      contents += buttons
    }

    val space = new Panel {
      override def paintComponent(g: Graphics2D) = {
        g.setColor(new Color(0, 0, 0))
        g.fillRect(0, 0, width, fullHeight)
        Simulation.draw(g)
      }
    }

    val settings = new BoxPanel(Orientation.Vertical) {


      val msg = new Label {
        override def paintComponent(g: Graphics2D): Unit = {
          g.setColor(new Color(0, 0, 0))
          g.drawString("Choose the parameters for your satellite", width / 25, height / 4 - 100)
        }
      }

      msg.minimumSize   = new Dimension(width, height / 4)
      msg.preferredSize = new Dimension(width, height / 4)
      msg.maximumSize   = new Dimension(width, height / 4)

      val xPrompt = new Label("Choose x-coordinate") {
        minimumSize   = new Dimension(width / 2 - 80, 40)
        preferredSize = new Dimension(width / 2 - 80, 40)
        maximumSize   = new Dimension(width / 2 - 80, 40)
      }

      val yPrompt = new Label("Choose y-coordinate") {
        minimumSize   = new Dimension(width / 2, 40)
        preferredSize = new Dimension(width / 2, 40)
        maximumSize   = new Dimension(width / 2, 40)
      }

      val positionPrompts = new BoxPanel(Orientation.Horizontal) {
        contents += xPrompt
        contents += yPrompt
      }

      val positions = Array[Double](-1.5, -1.4, -1.3, -1.2, -1.1, -1.0, -0.9, -0.8, -0.7, -0.6, -0.5,
                                     0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5)

      val xPosition = new ComboBox[Double](positions) {
        minimumSize   = new Dimension(width / 4, 160)
        preferredSize = new Dimension(width / 4, 160)
        maximumSize   = new Dimension(width / 4, 160)
      }

      val yPosition = new ComboBox[Double](positions) {
        minimumSize   = new Dimension(width / 4, 160)
        preferredSize = new Dimension(width / 4, 160)
        maximumSize   = new Dimension(width / 4, 160)
      }

      val spaceBetweenPositions = new Panel {
        minimumSize   = new Dimension(150, 1)
        preferredSize = new Dimension(150, 1)
        maximumSize   = new Dimension(150, 1)
      }

      val positionBoxes = new BoxPanel(Orientation.Horizontal) {
        contents += xPosition
        contents += spaceBetweenPositions
        contents += yPosition
      }

      val massPrompt = new Label("Choose mass") {
        minimumSize   = new Dimension(width / 2 - 80, 40)
        preferredSize = new Dimension(width / 2 - 80, 40)
        maximumSize   = new Dimension(width / 2 - 80, 40)
      }

      val velocityPrompt = new Label("Choose velocity") {
        minimumSize   = new Dimension(width / 2, 40)
        preferredSize = new Dimension(width / 2, 40)
        maximumSize   = new Dimension(width / 2, 40)
      }

      val massAndVelocityPrompts = new BoxPanel(Orientation.Horizontal) {
        contents += massPrompt
        contents += velocityPrompt
      }

      val minMass   = 1000
      val maxMass   = 1000000
      val startMass = 1000

      val mass = new Slider {
        orientation = Orientation.Horizontal
        min   = minMass
        max   = maxMass
        value = startMass
        majorTickSpacing = 100000
        paintTicks = true

        minimumSize   = new Dimension(300, 50)
        preferredSize = new Dimension(300, 50)
        maximumSize   = new Dimension(300, 50)

        val labelTable = HashMap[Int, Label]()
        labelTable += minMass       -> new Label("1000 kg")
        labelTable += maxMass / 2   -> new Label("500 000 kg")
        labelTable += maxMass       -> new Label("1 000 000 kg")

        labels = labelTable
        paintLabels = true

      }

      val minVelocity = 0
      val maxVelocity = 50000
      val startVelocity = 25000

      val velocity = new Slider {
        orientation = Orientation.Horizontal
        min   = minVelocity
        max   = maxVelocity
        value = startVelocity
        majorTickSpacing = 10000
        paintTicks = true

        minimumSize   = new Dimension(300, 50)
        preferredSize = new Dimension(300, 50)
        maximumSize   = new Dimension(300, 50)

        val labelTable = HashMap[Int, Label]()
        labelTable += minVelocity       -> new Label("0 m/s")
        labelTable += maxVelocity / 2   -> new Label("25 000 m/s")
        labelTable += maxVelocity       -> new Label("50 000 m/s")

        labels = labelTable
        paintLabels = true

      }

      val emptySpaceMassVel = new Panel {
        minimumSize = new Dimension(70, 1)
        preferredSize = new Dimension(70, 1)
        maximumSize = new Dimension(70, 1)
      }

      val massAndVelocity = new BoxPanel(Orientation.Horizontal) {
        minimumSize = new Dimension(width, 100)
        preferredSize = new Dimension(width, 100)
        maximumSize = new Dimension(width, 100)
        contents += mass
        contents += emptySpaceMassVel
        contents += velocity
      }

      val timestepPrompt = new Label("Choose how fast simulation progresses") {
        minimumSize   = new Dimension(width / 2 - 80, 40)
        preferredSize = new Dimension(width / 2 - 80, 40)
        maximumSize   = new Dimension(width / 2 - 80, 40)
      }

      val simulationTimePrompt = new Label("Choose simulation length in seconds") {
        minimumSize   = new Dimension(width / 2, 40)
        preferredSize = new Dimension(width / 2, 40)
        maximumSize   = new Dimension(width / 2, 40)
      }

      val stepPrompts = new BoxPanel(Orientation.Horizontal) {
        minimumSize = new Dimension(width, 40)
        preferredSize = new Dimension(width, 40)
        maximumSize = new Dimension(width, 40)
        contents += timestepPrompt
        contents += simulationTimePrompt
      }

      val speedsteps = Array[String]("Slow", "Normal", "Fast")

      val simulationStep = new ComboBox[String](speedsteps) {
        minimumSize   = new Dimension(width / 4, 160)
        preferredSize = new Dimension(width / 4, 160)
        maximumSize   = new Dimension(width / 4, 160)
      }

      val timesteps = Array[Int](5, 10, 20, 30, 45, 60, 90, 120)

      val simulationLength = new ComboBox[Int](timesteps) {
        minimumSize   = new Dimension(width / 4, 160)
        preferredSize = new Dimension(width / 4, 160)
        maximumSize   = new Dimension(width / 4, 160)
      }

      val spaceBetweenSteps = new Panel {
        minimumSize   = new Dimension(150, 1)
        preferredSize = new Dimension(150, 1)
        maximumSize   = new Dimension(150, 1)
      }

      val steps = new BoxPanel(Orientation.Horizontal) {
        contents += simulationStep
        contents += spaceBetweenSteps
        contents += simulationLength
      }

      val settingsStartButton = new Button("Start simulation")

      contents += msg
      contents += positionPrompts
      contents += positionBoxes
      contents += massAndVelocityPrompts
      contents += massAndVelocity
      contents += stepPrompts
      contents += steps
      contents += settingsStartButton
    }

    this.listenTo(startButton, settingsButton, settings.settingsStartButton)

    this.reactions += {
      case buttonClick: ButtonClicked =>
        if (buttonClick.source == startButton) {
          simLength = 2147483647
          timestep = timestep * 2
          contents = space
          timer.start()
        } else if (buttonClick.source == settingsButton) {
          contents = settings
        } else {
          val a = settings.xPosition.item
          val satMass = settings.mass.value
          val satPos = Vector(settings.xPosition.item * au, settings.yPosition.item * au)
          val satVel = if (settings.yPosition.item >= 0.0) {
            Vector(0.0, -settings.velocity.value.toDouble)
          } else {
            Vector(0.0, settings.velocity.value.toDouble)
          }
          val simStep = settings.simulationStep.item match {
            case "Slow"   => 1
            case "Normal" => 2
            case "Fast"   => 4
          }
          timestep = timestep * simStep
          simLength = settings.simulationLength.item * 100
          Simulation.astronomicalBodies += AstronomicalBody("Satellite", 3.0, satMass, satVel, satPos)
          contents = space
          timer.start()
        }
    }

    val listener = new ActionListener() {
      def actionPerformed(e : java.awt.event.ActionEvent) = {
        Simulation.step()
        simLength -= 1
        space.repaint()
        if (Simulation.checkCollision || simLength <= 0) quit()
      }
    }

    val timer = new javax.swing.Timer(10, listener)

    contents = start



  }

}
