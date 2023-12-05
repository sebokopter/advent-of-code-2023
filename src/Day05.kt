data class Line(val destination: Long, val source: Long, val length: Long) {

    companion object {
        operator fun invoke(input: String): Line {
            val (destination, source, length) = input.split(" ").map(String::toLong)
            return Line(destination, source, length)
        }
    }
}

data class Almanac(val categories: List<List<Line>>) {
    companion object {
        operator fun invoke(input: String): Almanac {
            val categories = input.split((".*map:\n").toRegex())
                .drop(1)
                .map {
                    it.trim().split("\n").map(Line::invoke)
                }
            return Almanac(categories)
        }
    }

}

fun main() {
    fun getSeeds(input: List<String>): List<Long> = input
        .first()
        .trim()
        .removePrefix("seeds: ")
        .split(" ")
        .map(String::toLong)

    fun getSeeds2(input: List<String>): List<Long> = getSeeds(input)
        .chunked(2)
        .flatMap { (start, length) ->
            start until start + length
        }

    fun lookup(seeds: List<Long>, almanac: Almanac): List<Long> = almanac.categories
        .fold(seeds) { inputList, categoryMap ->
            inputList.map { input ->
                categoryMap.forEach { (destination, source, length) ->
                    if (input in (source until source + length)) {
                        val offset = input - source
                        return@map destination + offset
                    }
                }
                input
            }
        }

    fun part1(input: List<String>): Long {
        val seeds = getSeeds(input)
        val almanac = Almanac(input.drop(2).joinToString("\n"))
        val locations = lookup(seeds, almanac)
        return locations.min()
    }

    fun part2(input: List<String>): Long {
        val seeds = getSeeds2(input)
        val almanac = Almanac(input.drop(2).joinToString("\n"))
        val locations = lookup(seeds, almanac)
        return locations.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
