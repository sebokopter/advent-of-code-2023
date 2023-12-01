fun main() {
    fun calibrationValue(line: String): Int {
        var first = ""
        var last = ""
        line.indices.forEach { index ->
            val char = line[index]
            if (char.isDigit()) {
                val string = char.toString()
                if (first.isEmpty()) first = string
                last = string
            } else {
                lettersSet.forEach { letters ->
                    if (line.substring(index, line.length).startsWith(letters)) {
                        val string = lettersToDigitMap.getValue(letters)
                        if (first.isEmpty()) first = string
                        last = string
                    }
                }
            }
        }
        return "$first$last".toInt()
    }

    fun sumOfCalibrationValues(input: List<String>) = input.sumOf(::calibrationValue)

    fun part1(input: List<String>): Int = sumOfCalibrationValues(input)

    fun part2(input: List<String>): Int = sumOfCalibrationValues(input)

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test_part1")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test_part2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

val lettersToDigitMap = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)
val lettersSet: Set<String> = setOf(
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
)
