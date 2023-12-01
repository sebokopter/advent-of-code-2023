fun main() {
    fun calibrationValue(line: String): Int {
        val (first, last) = line.indices.fold("" to "") { (first, last), index ->
            val (matchedDigit) = possibleDigits.matchAt(line, index)?.destructured ?: return@fold first to last
            // if not in the map it must be a single digit char
            val digitString = lettersToDigitMap[matchedDigit] ?: matchedDigit
            // an empty `first` indicates there was no match before
            first.ifEmpty { digitString } to digitString
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
val possibleDigits = "(\\d|${lettersToDigitMap.keys.joinToString("|")})".toRegex()
