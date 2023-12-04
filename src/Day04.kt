fun main() {
    fun String.removeRegexPrefix(regex: Regex) = replace(regex, "")
    fun Int.pow(exp: Int) = toBigInteger().pow(exp).toInt()

    fun matchingWinningNumbers(line: String): Int {
        val (winnerSet, ownSet) = line
            .removeRegexPrefix("Card \\d+:".toRegex())
            .replace("  ", " ")
            .split("|")
            .map { it.trim().split(" ") }
        return ownSet.count { it in winnerSet }
    }

    fun part1(input: List<String>): Int = input.sumOf { line ->
        val count = matchingWinningNumbers(line)
        if (count == 0) 0
        else 2.pow(count - 1)
    }

    fun part2(input: List<String>): Int =
        input.withIndex().fold(MutableList(input.size) { 1 }) { list, (cardId, line) ->
            val count = matchingWinningNumbers(line)
            for (offset in 1..count) {
                val cards = list[cardId]
                list[cardId + offset] = list[cardId + offset] + cards
            }
            list
        }.sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
