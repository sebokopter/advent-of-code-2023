import kotlin.math.abs

fun main() {
    fun differences(input: List<Int>): MutableList<MutableList<Int>> {
        val differences = mutableListOf(input.toMutableList())
        do {
            val newDifference = differences.last().windowed(2, 1) { (a, b) ->
                abs(b - a)
            }
            differences.add(newDifference.toMutableList())
        } while (!differences.last().all { it == 0 })
        return differences
    }

    fun extrapolate(input: List<Int>): Int {
        val differences = differences(input)
        println(differences)
        val reversed: MutableList<MutableList<Int>> = differences.reversed().toMutableList()

        reversed.first().add(0)
        var last = 0
        println(reversed)
        reversed.windowed(2, 1) { (a, b) ->
            if (a.isEmpty()) println(a.last())
            if (b.isEmpty()) println(b.last())
            last = a.last() + b.last()
            b.add(last)
        }
        return last
    }

    fun part1(input: List<String>): Int {
        val histories = input.map { line ->
            line.split(" ").map { it.toInt() }
        }
        return histories.sumOf {
            extrapolate(it)
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
//    check(part2(testInput) == 0)

    val input = readInput("Day09")
    part1(input).println()
//    part2(input).println()
}
