typealias Area = MutableList<MutableList<Char>>
//defined in Day03 already
//typealias Coordinate = Pair<Int, Int> // y (top to bottom) to x (left to right)

fun main() {
    fun findStartingPoint(area: Area): Coordinate {
        area.indices.forEach { y ->
            area[y].indices.forEach { x ->
                if (area[y][x] == 'S') return y to x
            }
        }
        error("There's no 'S' in this area: $area")
    }

    fun possibleConnections(startingPoint: Coordinate, area: Area): List<Pair<Int, Int>> {
        val (y, x) = startingPoint
        val neighbors = buildList<Coordinate> {
            if (y - 1 >= 0) add(y - 1 to x) // north
            if (y + 1 <= area.lastIndex) add(y + 1 to x) // south
            if (x + 1 <= area.lastIndex) add(y to x + 1) //east
            if (x - 1 >= 0) add(y to x - 1) //west
        }
        return neighbors.filter { (y, x) -> area[y][x] != '.' }
    }

    fun Coordinate.isEast(neighbor: Coordinate): Boolean {
        val (y, x) = this
        val east = y to (x + 1)
        return neighbor == east
    }

    fun Coordinate.isWest(neighbor: Coordinate): Boolean {
        val (y, x) = this
        val potentialNeighbor = y to (x - 1)
        return neighbor == potentialNeighbor
    }

    fun Coordinate.isSouth(neighbor: Coordinate): Boolean {
        val (y, x) = this
        val potentialNeighbor = (y + 1) to x
        return neighbor == potentialNeighbor
    }

    fun Coordinate.isNorth(neighbor: Coordinate): Boolean {
        val (y, x) = this
        val potentialNeighbor = (y - 1) to x
        return neighbor == potentialNeighbor
    }

    fun Coordinate.isHorizontal(neighbor: Coordinate): Boolean = neighbor.first == first
    fun Coordinate.isVertical(neighbor: Coordinate): Boolean = neighbor.second == second
    fun Coordinate.isL(neighbor: Coordinate): Boolean = isNorth(neighbor) || isEast(neighbor)
    fun Coordinate.isJ(neighbor: Coordinate): Boolean = isNorth(neighbor) || isWest(neighbor)
    fun Coordinate.is7(neighbor: Coordinate): Boolean = isSouth(neighbor) || isWest(neighbor)
    fun Coordinate.isF(neighbor: Coordinate): Boolean = isSouth(neighbor) || isEast(neighbor)

    fun processNeighbors(
        startingPoint: Coordinate,
        nextStartingPoints: MutableList<Coordinate>,
        area: Area,
        distances: MutableList<MutableList<Long>>,
        currentIteration: Long,
        predicate: (Pair<Int, Int>) -> Boolean,
    ) {
        val possibleConnections = possibleConnections(startingPoint, area)
        possibleConnections.filter(predicate).forEach { (y, x) ->
            nextStartingPoints.add(y to x)
            distances[y][x] = currentIteration
        }
        area[startingPoint.first][startingPoint.second] = '.' // remove element, so that we don't visit it multiple times
    }

    fun calculateDistance(
        area: Area,
        startingPoints: List<Coordinate>,
        distances: MutableList<MutableList<Long>>,
        currentIteration: Long
    ): MutableList<MutableList<Long>> {
        if (startingPoints.isEmpty()) return distances
        val nextStartingPoints: MutableList<Coordinate> = mutableListOf()
        startingPoints.forEach { startingPoint ->
            val (y, x) = startingPoint
            val predicate: (Pair<Int, Int>) -> Boolean = when (area[y][x]) {
                'S' -> {
                    {
                        val (y, x) = it
                        val c = area[y][x]
                        startingPoint.isEast(it) && (c == '-' || c == 'J' || c == '7') ||
                                startingPoint.isWest(it) && (c == '-' || c == 'L' || c == 'F') ||
                                startingPoint.isNorth(it) && (c == '|' || c == 'F' || c == '7') ||
                                startingPoint.isSouth(it) && (c == '|' || c == 'L' || c == 'J')
                    }
                }

                '|' -> {
                    { startingPoint.isVertical(it) }
                }

                '-' -> {
                    { startingPoint.isHorizontal(it) }
                }

                'L' -> {
                    { startingPoint.isL(it) }
                }

                'J' -> {
                    { startingPoint.isJ(it) }
                }

                '7' -> {
                    { startingPoint.is7(it) }
                }

                'F' -> {
                    { startingPoint.isF(it) }
                }

                else -> {
                    { false }/*ignore non-pipe fields*/
                }
            }
            processNeighbors(startingPoint, nextStartingPoints, area, distances, currentIteration, predicate)
        }
        return calculateDistance(area, nextStartingPoints, distances, currentIteration + 1)
    }

    fun part1(input: List<String>): Long {
        val areaSize = input.size
        val area: Area = input.map { it.toCharArray().toMutableList() }.toMutableList()
        val distances = MutableList(areaSize) { MutableList(areaSize) { 0L } }
        val startingPoint = findStartingPoint(area)
        calculateDistance(area, listOf(startingPoint), distances, 1)
        return distances.maxOf { it.maxOf { it } }
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day10_test1")
    val testInput2 = readInput("Day10_test2")
    check(part1(testInput1) == 4L)
    check(part1(testInput2) == 8L)
//    check(part2(testInput) == 0)

    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}
