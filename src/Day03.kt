typealias Coordinate = Pair<Int, Int> // x (left to right) to y (top to bottom)

fun main() {
    fun findParts(input: List<String>, gearsOnly: Boolean = false): List<Coordinate> =
        input.withIndex().fold(listOf<Coordinate>()) { coordinates, (row, line) ->
            line.withIndex().fold(coordinates) { coordinates, (column, coordinate) ->
                val regex = Regex(if (gearsOnly) "\\*" else "[^0-9.]")
                if (regex.matches(coordinate.toString())) coordinates + Coordinate(column, row)
                else coordinates
            }
        }

    fun part1(input: List<String>): Int {
        val partsCoordinates = findParts(input)
        return input.withIndex().sumOf { (row, line) ->
            "\\d+".toRegex().findAll(line).sumOf {
                val rowRange = (row - 1).coerceAtLeast(0)..(row + 1).coerceAtMost(input.lastIndex)
                val columnRange =
                    (it.range.first - 1).coerceAtLeast(0)..(it.range.last + 1).coerceAtMost(line.lastIndex)
                val isPartNumber = partsCoordinates.any { (column, row) ->
                    row in rowRange && column in columnRange
                }
                if (isPartNumber) it.value.toInt() else 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val partsCoordinates = input.withIndex().fold(findParts(input, gearsOnly = true).associateWith { emptyList<Int>() }.toMutableMap()) { partsCoordinates, (row, line) ->
            "\\d+".toRegex().findAll(line).forEach {
                val rowRange = (row - 1).coerceAtLeast(0)..(row + 1).coerceAtMost(input.lastIndex)
                val columnRange =
                    (it.range.first - 1).coerceAtLeast(0)..(it.range.last + 1).coerceAtMost(line.lastIndex)
                partsCoordinates.keys.forEach { coordinate ->
                    val (column, gearRow) = coordinate
                    val isGearNumber = gearRow in rowRange && column in columnRange
                    if (isGearNumber) partsCoordinates[coordinate] = partsCoordinates.getValue(coordinate) + it.value.toInt()
                }
            }
            partsCoordinates
        }
        return partsCoordinates.values.sumOf { list ->
            if (list.size != 2) return@sumOf 0
            list[0] * list[1]
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
