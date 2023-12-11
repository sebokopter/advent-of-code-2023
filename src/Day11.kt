import kotlin.math.abs

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
            expanded.add(line)
            if (line.none { it == '#' }) expanded.add(line)
            expanded
        }
    }

    fun expand2Dimension(input: List<List<Char>>): List<List<Char>> {
        val oneDimensionExpanded = expand1Dimension(input)
        val swapImage = swapImage(oneDimensionExpanded)
        val twoDimensionsExpanded = expand1Dimension(swapImage)
        return swapImage(twoDimensionsExpanded)
    }

    fun countGalaxies(input: List<List<Char>>): List<Coordinate> {
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

    fun shortestPath(b: Coordinate, a: Coordinate) = abs(b.first - a.first) + abs(b.second - a.second)

    fun sumShortestPaths(pairs: List<Pair<Coordinate, Coordinate>>): Int = pairs.sumOf { (a, b) ->
        shortestPath(b, a)
    }

    fun part1(input: List<String>): Int {
        val expanded = expand2Dimension(input.map { it.toList() })
        val res = countGalaxies(expanded)
        val allPairs = generatePairs(res)
        return sumShortestPaths(allPairs)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374)
//    check(part2(testInput) == 0)

    val input = readInput("Day11")
    part1(input).println()
//    part2(input).println()
}
