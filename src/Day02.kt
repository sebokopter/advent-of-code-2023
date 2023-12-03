data class Game(val gameId: Int, val setsOfCubes: List<SetOfCubes>) {
    companion object {
        operator fun invoke(line: String): Game {
            val (string1, string2) = line.split(": ")
            val (_, id) = string1.split(" ")
            val setsOfCubes = string2.split("; ").map { SetOfCubes(it) }
            return Game(id.toInt(), setsOfCubes)
        }
    }
}

data class SetOfCubes(val cubes: Set<Cubes>) {
    companion object {
        operator fun invoke(input: String): SetOfCubes {
            val cubes = input.split(", ")
            return SetOfCubes(cubes.map {
                val (amount, color) = it.split(" ")
                Cubes(amount.toInt(), color)
            }.toSet())
        }
    }
}

data class Cubes(val amount: Int, val color: String)

fun main() {
    fun part1(input: List<String>): Int = input
        .map(Game::invoke)
        .filter { game ->
            game.setsOfCubes.all { setOfCubes ->
                setOfCubes.cubes.all { (amount, color) ->
                    amount <= maxCubes.getValue(color)
                }
            }
        }
        .sumOf { it.gameId }

    fun part2(input: List<String>): Int = input
        .map(Game::invoke)
        .sumOf { game ->
            val maxColors = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            game.setsOfCubes.forEach { setOfCubes ->
                setOfCubes.cubes.forEach { (amount, color) ->
                    maxColors[color] = maxOf(amount, maxColors.getValue(color))
                }
            }
            maxColors.values.reduce(Int::times)
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
