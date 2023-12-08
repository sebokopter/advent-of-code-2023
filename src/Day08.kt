import java.math.BigInteger

fun main() {
    fun parseNodeInput(input: String): Pair<String, Map<String, Pair<String, String>>> {
        val (leftRightInstruction, nodesInput) = input.split("\n\n")
        val nodes = nodesInput.lines().associate { line ->
            val (first, second) = line.split(" = ")
            val nodes = second.removeSurrounding("(", ")").split(", ")
            first to (nodes[0] to nodes[1])
        }
        return leftRightInstruction to nodes
    }

    fun part1(input: String): Int {
        val (leftRightInstruction, nodes) = parseNodeInput(input)
        var currentNode = "AAA"
        var instructionIndex = 0
        var steps = 0
        do {
            val instruction = leftRightInstruction[instructionIndex]
            val nextNode = if (instruction == 'L') {
                nodes.getValue(currentNode).first
            } else {
                nodes.getValue(currentNode).second
            }
            currentNode = nextNode
            instructionIndex++
            if (instructionIndex > leftRightInstruction.lastIndex) instructionIndex = 0
            steps++
        } while (currentNode != "ZZZ")
        return steps
    }

    fun findLargestCommonMultiple(a: BigInteger, b: BigInteger): BigInteger {
        val greaterNumber = a.max(b)
        val largestMultiple = a * b
        val factors = listOf(a, b)
        var largestCommonMultiple = greaterNumber
        while (largestCommonMultiple <= largestMultiple) {
            if (factors.all { factor -> largestCommonMultiple % factor == BigInteger.ZERO }) return largestCommonMultiple
            largestCommonMultiple += greaterNumber
        }
        // no smaller common multiple found
        return largestMultiple
    }

    fun part2(input: String): BigInteger {
        val (leftRightInstruction, nodes) = parseNodeInput(input)
        val currentNodes = nodes.keys.filter { it.endsWith("A") }
        // variant 1: brute force
//        var instructionIndex = 0
//        var steps = 0
//        do  {
//            val instruction = leftRightInstruction[instructionIndex]
//            currentNodes = currentNodes.map { currentNode ->
//                val nextNode = if (instruction == 'L') {
//                    nodes.getValue(currentNode).first
//                } else {
//                    nodes.getValue(currentNode).second
//                }
//                nextNode
//            }
//            instructionIndex++
//            if (instructionIndex > leftRightInstruction.lastIndex) instructionIndex = 0
//            steps++
//        } while(!currentNodes.all { it.endsWith("Z") })

        // variant 2: multiplying step counts
        val stepsToEndNodes = currentNodes.map { startNode ->
            println("startNode: $startNode")
            var instructionIndex = 0
            var steps = 0.toBigInteger()
            var currentNode = startNode
            do {
                val instruction = leftRightInstruction[instructionIndex]
                val nextNode = if (instruction == 'L') {
                    nodes.getValue(currentNode).first
                } else {
                    nodes.getValue(currentNode).second
                }
                currentNode = nextNode
                instructionIndex++
                if (instructionIndex > leftRightInstruction.lastIndex) instructionIndex = 0
                steps++
            } while (!currentNode.endsWith("Z"))
            steps
        }
        val res = stepsToEndNodes.fold(1.toBigInteger()) { total, steps ->
            findLargestCommonMultiple(total, steps)
        }
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readText("Day08_test_1")
    val testInput2 = readText("Day08_test_2")
    val testInput3 = readText("Day08_test_3")
    check(part1(testInput1) == 2)
    check(part1(testInput2) == 6)
    check(part2(testInput3) == 6.toBigInteger())

    val input = readText("Day08")
    part1(input).println()
    part2(input).println()
}
