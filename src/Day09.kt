fun main() {
    fun differences(input: List<Long>): MutableList<MutableList<Long>> {
        val differences = mutableListOf(input.toMutableList())
        do {
            val newDifference = differences.last().windowed(2, 1) { (a, b) ->
                b - a
            }
            differences.add(newDifference.toMutableList())
        } while (!differences.last().all { it == 0L })
        return differences
    }

    fun extrapolate(input: List<Long>, extrapolateBeginning: Boolean = false): Long {
        val differences = differences(input)
        val reversed: MutableList<MutableList<Long>> = differences.reversed().toMutableList()
        reversed.first().add(0)
        var extrapolatedItem = 0.toLong()
        reversed.windowed(2, 1) { (a, b) ->
            extrapolatedItem = if (extrapolateBeginning) b.first() - a.first() else a.last() + b.last()
            val res = if (extrapolateBeginning) mutableListOf(extrapolatedItem) + b else b + extrapolatedItem
            b.clear()
            b.addAll(res)
        }
        return extrapolatedItem
    }

    fun part1(input: List<String>): Long {
        val histories = input.map { line -> line.split(" ").map { it.toLong() } }
        return histories.sumOf(::extrapolate)
    }

    fun part2(input: List<String>): Long {
        val histories = input.map { line -> line.split(" ").map { it.toLong() } }
        return histories.sumOf { extrapolate(it, true) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
