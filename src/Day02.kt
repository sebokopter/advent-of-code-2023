fun main() {
    fun List<String>.sumUpGames(block: (Int, List<Pair<Int, String>>) -> Int): Int = sumOf { line ->
        val (gameId, game) = line.split(": ")
        val (_, id) = gameId.split(" ")
        val cubes = game.split("; ").flatMap { setOfCubes ->
            val cubes = setOfCubes.split(", ")
            cubes.map {
                val (amount, color) = it.split(" ")
                val pair = amount.toInt() to color
                pair
            }
        }
        block(id.toInt(), cubes)
    }

    fun part1(input: List<String>): Int {
        return input.sumUpGames { id, cubes ->
            val validInput = cubes.all { (amount, color) ->
                amount <= maxCubes.getValue(color)
            }
            id.takeIf { validInput } ?: 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumUpGames { _, cubes ->
            val maxColors = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            cubes.forEach { (amount, color) ->
                maxColors[color] = maxOf(amount, maxColors.getValue(color))
            }
            maxColors.values.reduce { acc, value -> acc * value }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

val maxCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)
