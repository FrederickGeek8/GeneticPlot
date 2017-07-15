import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import javafx.stage.Stage

class GeneticPlot : Application() {
    companion object {

        fun MinkowskiDistance(first: Array<Int>, second: Array<Int>, p:Double = 1.0): Double {
            var value = 0.0
            for (i in first.indices){
                value += Math.pow(Math.abs(first[i] - second[i]).toDouble(), p)
            }
            return Math.pow(value, 1/p)
        }

        val series1: Series<Number, Number> = Series()
        // For some reason I have to add @JvmStatic in order for my main to be recognized
        @JvmStatic fun main(args: Array<String>) {
            val objective = arrayOf(21, 34)
            val numActors = 16
            val numGenerations = 1000
            val allowedSteps = 100 // You must also change DNA length in Actor.kt

            var actors: MutableList<Actor> = arrayListOf()

            for (i in 1..numActors) {
                actors.add(Actor(0, 0))
            }

            for (i in 1..allowedSteps) {
                for (actor in actors) {
                    actor.step()
                }
            }

            actors.sortBy { MinkowskiDistance(it.getLocation(), objective) }

            println("Initial population: " + actors)

            var generations = 0

            while (generations < numGenerations) {


                val culled = actors.slice(IntRange(0, 8))
                val children: MutableList<Actor> = arrayListOf()

                for (i in (0..culled.size - 2)) {
//        actor.step()
                    children.add(culled[i].breed(actors[i + 1]))
                    children.add(culled[i + 1].breed(actors[i]))
                }

                for (i in 1..allowedSteps) {
                    for (child in children) {
                        child.step()
                    }
                }

                children.sortBy { MinkowskiDistance(it.getLocation(), objective) }

                actors = children

                for (child in actors) {
                    series1.data.add(XYChart.Data(generations, MinkowskiDistance(child.getLocation(), objective)))
                }

                generations++

            }

            println("After $numGenerations generations: " + actors)

            javafx.application.Application.launch(GeneticPlot::class.java)
        }
    }

    override fun start(stage: Stage) {
        stage.title = "Scatter Chart Sample"
        val xAxis = NumberAxis(0.0, 1000.0, 100.0)
        val yAxis = NumberAxis(0.0, 100.0, 10.0)
        val sc = ScatterChart(xAxis, yAxis)
        xAxis.label = "Age (generations)"
        yAxis.label = "Minkowski Distance"
        sc.title = "Genetic Pathfinding Algorithm"

        sc.data.addAll(series1)
        val scene = Scene(sc, 800.0, 400.0)
        stage.scene = scene
        stage.show()
    }

}