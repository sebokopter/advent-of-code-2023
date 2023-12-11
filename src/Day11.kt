import kotlin.math.abs
import kotlin.math.exp

val testExpanded = """
|....#........
|.........#...
|#............
|.............
|.............
|........#....
|.#...........
|............#
|.............
|.............
|.........#...
|#....#.......
""".trimMargin()

private const val emptySpaceMarker = 'x'

fun main() {

    fun swapImage(current: List<List<Char>>): List<List<Char>> {
        val new = MutableList(current.first().size) { MutableList(current.size) { '.' } }
        current.withIndex().forEach { (y, row) ->
            row.withIndex().forEach { (x, column) ->
                new[x][y] = column
            }
        }
        return new
    }

    fun expand1Dimension(original: List<List<Char>>): List<List<Char>> {
        return original.fold(mutableListOf()) { expanded, line ->
            if (line.any { it == '#' }) {
                expanded.add(line)
            } else {
                expanded.add(line.map { emptySpaceMarker })
            }
            expanded
        }
    }

    fun expand2Dimension(input: List<List<Char>>): List<List<Char>> {
        val oneDimensionExpanded = expand1Dimension(input)
        val swapImage = swapImage(oneDimensionExpanded)
        val twoDimensionsExpanded = expand1Dimension(swapImage)
        return swapImage(twoDimensionsExpanded)
    }

    fun numberGalaxies(input: List<List<Char>>): List<Coordinate> {
        val galaxies: MutableList<Coordinate> = mutableListOf()
        input.withIndex().forEach { (y, line) ->
            line.withIndex().forEach { (x, cell) ->
                if (cell == '#') galaxies.add(y to x)
            }
        }
        return galaxies
    }

    fun generatePairs(res: List<Coordinate>): List<Pair<Coordinate, Coordinate>> = buildList {
        for (start in 0 until res.lastIndex) {
            for (end in start + 1..res.lastIndex) {
                add(res[start] to res[end])
            }
        }
    }

    fun shortestPath(b: Coordinate, a: Coordinate, expansionFactor: Int = 2, image: List<List<Char>>): Int {
        println(image)
        val horizontalEmptyColumns =
            image[minOf(a.first, b.first)]
                .subList(minOf(a.second, b.second), maxOf(a.second, b.second))
                .count { it == emptySpaceMarker }
        val verticalEmptyRows = image
            .subList(minOf(a.first, b.first), maxOf(a.first, b.first))
            .count { it[minOf(a.second, b.second)] == emptySpaceMarker }
        return abs(b.first - a.first) + abs(b.second - a.second) + (verticalEmptyRows * expansionFactor) + (horizontalEmptyColumns * expansionFactor)
    }

    fun sumShortestPaths(
        pairs: List<Pair<Coordinate, Coordinate>>,
        expansionFactor: Int = 2,
        image: List<List<Char>>
    ): Int = pairs.sumOf { (a, b) ->
        val shortestPath = shortestPath(a, b, expansionFactor, image)
        println(shortestPath)
        shortestPath
    }

    fun part1(input: List<String>, expansionFactor: Int = 2): Int {
        val expanded = expand2Dimension(input.map { it.toList() })
        val res = numberGalaxies(expanded)
        val allPairs = generatePairs(res)
        val sumShortestPaths = sumShortestPaths(allPairs, expansionFactor, expanded)
        sumShortestPaths.println()
        return sumShortestPaths
    }

    fun part2(input: List<String>, expansionFactor: Int): Int {
        val expanded = expand2Dimension(input.map { it.toList() })
        val res = numberGalaxies(expanded)
        val allPairs = generatePairs(res)
        val sumShortestPaths = sumShortestPaths(allPairs, expansionFactor, expanded)
        println(sumShortestPaths)
        return sumShortestPaths
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)
    check(part2(testInput, 10) == 1030)
    check(part2(testInput, 100) == 8410)

    val input = readInput("Day11")
    part1(input).println()
    part2(input, 1_000_000).println()
}
