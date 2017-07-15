import java.security.SecureRandom

class Actor (var x: Int, var y: Int, myDNA: String? = null) {
    val DNA: String
    private var steps: Int = 0
    init {
        var random = SecureRandom()
        if (myDNA == null) {
            var newDNA = ""
            for (i in 1..101) {
                newDNA += random.nextInt(4).toString()
            }
            DNA = newDNA
        } else {
            DNA = myDNA
        }
    }

    override fun toString(): String = "Actor at x:$x, y:$y with DNA $DNA"

    fun getLocation(): Array<Int> {
        return arrayOf(x, y)
    }

    fun breed(otherActor: Actor): Actor {
        val newDNA = StringBuilder(otherActor.DNA.substring(0, 23) + this.DNA.substring(23, this.DNA.length))
        val random = SecureRandom()
//        println(newDNA)
        for (i in newDNA.indices) {
            if (random.nextDouble() < 0.03) {
                newDNA[i] = random.nextInt(4).toString().toCharArray()[0]
            }
        }

        return Actor(0, 0, newDNA.toString())
    }

    fun step() {
        if (steps < DNA.length) {
            when (DNA[steps].toString().toInt()) {
                0 -> x++
                1 -> x--
                2 -> y++
                3 -> y--
                else -> println(DNA[steps].toString().toInt())
            }
            steps++
        }
    }
}