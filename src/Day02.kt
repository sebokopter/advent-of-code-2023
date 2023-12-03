fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (gameId, game) = line.split(": ".toRegex())
            val (_, id) = gameId.split(" ")
            val setsOfCubes = game.split("; ")
            val validInput = setsOfCubes.all { setOfCubes ->
                val cubes = setOfCubes.split(", ")
                cubes.all {
                    val (amount, color) = it.split(" ")
                    when (color) {
                        "red" -> amount.toInt() <= 12
                        "green" -> amount.toInt() <= 13
                        "blue" -> amount.toInt() <= 14
                        else -> error("Unknown color")
                    }
                }
            }
            if (validInput) id.toInt() else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val (gameId, game) = line.split(": ".toRegex())
            val (_, id) = gameId.split(" ")
            var maxRed = 0
            var maxGreen = 0
            var maxBlue = 0
            val setsOfCubes = game.split("; ")
            setsOfCubes.forEach { setOfCubes ->
                val cubes = setOfCubes.split(", ")
                cubes.forEach {
                    val (amount, color) = it.split(" ")
                    when (color) {
                        "red" -> maxRed = maxOf(amount.toInt(),maxRed)
                        "green" -> maxGreen = maxOf(amount.toInt(),maxGreen)
                        "blue" -> maxBlue = maxOf(amount.toInt(),maxBlue)
                        else -> error("Unknown color")
                    }
                }
            }
            maxRed*maxGreen*maxBlue
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
