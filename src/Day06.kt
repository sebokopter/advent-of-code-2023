data class Race(val time: Long, val distance: Long)

fun main() {
    fun parseRaces(input: List<String>): List<Race> {
        val (times, distances) = input.map { line ->
            val seq = "\\d+".toRegex().findAll(line).toList()
            seq.map { it.value.toLong() }
        }
        return times.indices.map {index ->
            Race(times[index], distances[index])
        }
    }

    // calculate number of ways to win
    fun numberOfWaysToWin(race: Race): Long {
        var numberOfWaysToWin = 0L
        for (speed in 0..race.time) {
            val timeLeft = race.time - speed
            val distance = timeLeft * speed
            if (distance > race.distance) numberOfWaysToWin++
        }
        return numberOfWaysToWin
    }

    fun part1(input: List<String>): Long {
        val races = parseRaces(input)
        return races.fold(1) { multiplied, race ->
            // if there is no way to win just return multiplied
            multiplied * numberOfWaysToWin(race).coerceAtLeast(1L)
        }
    }

    fun part2(input: List<String>): Long {
        val (race) = parseRaces(input.map { it.replace(" ","") })
        return numberOfWaysToWin(race)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
