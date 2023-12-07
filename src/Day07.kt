val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
val cardsWithJoker = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
val cardsWithoutJoker = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
val cardsWithoutJokerString = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2")

enum class PrioType(val prio: Int) {
    HighCard(prio = 1),
    OnePair(prio = 2),
    TwoPair(prio = 3),
    ThreeOfAKind(prio = 4),
    FullHouse(prio = 5),
    FourOfAKind(prio = 6),
    FiveOfAKind(prio = 7)
}

fun main() {

    operator fun List<String>.times(other: List<String>): List<String> =
        flatMap { item1 -> other.map { item2 -> item1 + item2 } }

    fun cardsWithoutJokerCombinations(amount: Int): List<String> {
        require(amount in 1 .. 5) { "amount needs to be between 1 and 5 (inclusive)" }
        return (1 until amount).fold(cardsWithoutJokerString) { acc, _ ->
            acc * cardsWithoutJokerString
        }
    }

    fun compareHighCards(firstHand: String, secondHand: String, cardSet: List<Char>): Int {
        for (index in firstHand.indices) {
            val card1 = firstHand[index]
            val card2 = secondHand[index]
            val cardStrength1 = cardSet.indexOf(card1)
            val cardStrength2 = cardSet.indexOf(card2)
            if (cardStrength1 == cardStrength2) continue
            return if (cardStrength1 < cardStrength2) 1 // higher index => weaker card prio
            else -1
        }
        return 0
    }


    fun getType(hand: String): PrioType {
        val amounts = cards.associateWith { card ->
            hand.count { it == card }
        }
        val sortedByAmount =
            amounts.asIterable().sortedByDescending { (_, amount) -> amount }.filter { (_, amount) -> amount >= 1 }
        val amountOfMostCards = sortedByAmount.first().value
        return when (sortedByAmount.size) {
            1 -> PrioType.FiveOfAKind
            2 -> if (amountOfMostCards == 4) PrioType.FourOfAKind else PrioType.FullHouse
            3 -> if (amountOfMostCards == 3) PrioType.ThreeOfAKind else PrioType.TwoPair
            4 -> if (amountOfMostCards == 2) PrioType.OnePair else PrioType.HighCard
            5 -> PrioType.HighCard
            else -> error("Can not happen")
        }
    }

    fun getTypeWithJoker(hand: String): PrioType {
        val jokerAmount = hand.count { card -> card == 'J' }
        val handWithoutJoker = hand.replace("J", "")
        return when (jokerAmount) {
            0 -> getType(hand)
            5 -> PrioType.FiveOfAKind
            else -> {
                check(jokerAmount in 0..5) {"There can at most be 5 joker"}
                val maxPrio = cardsWithoutJokerCombinations(jokerAmount).maxOf { cards ->
                    getType(handWithoutJoker + cards).prio
                }
                PrioType.entries.first { it.prio == maxPrio }
            }
        }
    }

    fun wins(firstHand: String, secondHand: String, cardSet: List<Char>): Int {
        val typeOfFirstHand = getType(firstHand)
        val typeOfSecondHand = getType(secondHand)
        if (typeOfFirstHand.prio > typeOfSecondHand.prio) return 1
        if (typeOfFirstHand.prio < typeOfSecondHand.prio) return -1
        return compareHighCards(firstHand, secondHand, cardSet)
    }
    fun winsWithJoker(firstHand: String, secondHand: String, cardSet: List<Char>): Int {
        val typeOfFirstHand = getTypeWithJoker(firstHand)
        val typeOfSecondHand = getTypeWithJoker(secondHand)
        if (typeOfFirstHand.prio > typeOfSecondHand.prio) return 1
        if (typeOfFirstHand.prio < typeOfSecondHand.prio) return -1
        return compareHighCards(firstHand, secondHand, cardSet)
    }

    fun part1(input: List<String>): Int {
        val handBids = input.map { line ->
            val (hand, bid) = line.split(" ")
            hand to bid.toInt()
        }
        val sortedHandBids = handBids.sortedWith(Comparator { (firstHand, _), (secondHand, _) ->
            return@Comparator wins(firstHand, secondHand, cards)
        })
        return sortedHandBids.withIndex().fold(0) { totalWinnings, (index, pair) ->
            val (_, bid) = pair
            val rank = index + 1
            totalWinnings + rank * bid
        }
    }

    fun part2(input: List<String>): Int {
        val handBids = input.map { line ->
            val (hand, bid) = line.split(" ")
            hand to bid.toInt()
        }
        val sortedHandBids = handBids.sortedWith(Comparator { (firstHand, _), (secondHand, _) ->
            return@Comparator winsWithJoker(firstHand, secondHand, cardsWithJoker)
        })
        return sortedHandBids.withIndex().fold(0) { totalWinnings, (index, pair) ->
            val (_, bid) = pair
            val rank = index + 1
            totalWinnings + rank * bid
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day07_test")
    check(part1(testInput1) == 6440)
    val testInput2 = readInput("Day07_test_part2")
    check(part2(testInput2) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

