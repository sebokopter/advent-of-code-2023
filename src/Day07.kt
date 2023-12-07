val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
val cardsWithJoker = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

enum class PrioType(val prio: Int) {
    HighCard(prio = 1), OnePair(prio = 2), TwoPair(prio = 3), ThreeOfAKind(prio = 4), FullHouse(prio = 5), FourOfAKind(
        prio = 6
    ),
    FiveOfAKind(prio = 7)
}

fun main() {
    fun compareHighCards(firstHand: String, secondHand: String): Int {
        for (index in firstHand.indices) {
            val card1 = firstHand[index]
            val card2 = secondHand[index]
            val cardStrength1 = cards.indexOf(card1)
            val cardStrength2 = cards.indexOf(card2)
            if (cardStrength1 == cardStrength2) continue
            return if (cardStrength1 < cardStrength2) 1 // higher index => weaker card prio
            else -1
        }
        return 0
    }
    fun compareHighCardsWithJoker(firstHand: String, secondHand: String): Int {
        for (index in firstHand.indices) {
            val card1 = firstHand[index]
            val card2 = secondHand[index]
            val cardStrength1 = cardsWithJoker.indexOf(card1)
            val cardStrength2 = cardsWithJoker.indexOf(card2)
            if (cardStrength1 == cardStrength2) continue
            return if (cardStrength1 < cardStrength2) 1 // higher index => weaker card prio
            else -1
        }
        return 0
    }

    fun getTypeWithJoker(hand: String): PrioType {
        val amounts = cards.associateWith { card ->
            hand.count { it == card }
        }
        val sorted =
            amounts.asIterable().sortedByDescending { (_, amount) -> amount }.filter { (_, amount) -> amount >= 1 }
        val amountOfMostCards = sorted.first().component2()
        val jokerAmount = sorted.firstOrNull() { (card, amount) -> card == 'J' }?.component2() ?: 0
        val hasJoker = jokerAmount > 0
        return when (sorted.size) {
            1 -> PrioType.FiveOfAKind
            2 -> if (hasJoker) return PrioType.FiveOfAKind else {
                if (amountOfMostCards == 4) {
                    PrioType.FourOfAKind
                } else {
                    PrioType.FullHouse
                }
            }

            3 -> if (amountOfMostCards == 3) {
                when (jokerAmount) {
                    3 -> if (sorted[1].component2() == 2) PrioType.FiveOfAKind else PrioType.FourOfAKind
                    2 -> PrioType.FiveOfAKind
                    1 -> PrioType.FourOfAKind
                    0 -> PrioType.ThreeOfAKind
                    else -> error("wrong joker calculation")
                }
            } else {
                when (jokerAmount) {
                    2 -> PrioType.FourOfAKind
                    1 -> if (sorted[1].component2() == 2) PrioType.FullHouse else PrioType.ThreeOfAKind
                    0 -> PrioType.TwoPair
                    else -> error("wrong joker calculation")
                }
            }

            4 -> PrioType.OnePair

            5 -> PrioType.HighCard
            else -> error("Can not happen")
        }

    }
    fun getType(hand: String): PrioType {
        val amounts = cards.associateWith { card ->
            hand.count { it == card }
        }
        val sorted =
            amounts.asIterable().sortedByDescending { (_, amount) -> amount }.filter { (_, amount) -> amount >= 1 }
        val amountOfMostCards = sorted.first().component2()
        return when (sorted.size) {
            1 -> PrioType.FiveOfAKind
            2 -> if (amountOfMostCards == 4) PrioType.FourOfAKind else PrioType.FullHouse
            3 -> if (amountOfMostCards == 3) PrioType.ThreeOfAKind else PrioType.TwoPair
            4 -> if (amountOfMostCards == 2) PrioType.OnePair else PrioType.HighCard
            5 -> PrioType.HighCard
            else -> error("Can not happen")
        }
    }
    fun wins(firstHand: String, secondHand: String): Int {
        val typeOfFirstHand = getType(firstHand)
        val typeOfSecondHand = getType(secondHand)
        if (typeOfFirstHand.prio > typeOfSecondHand.prio) return 1
        if (typeOfFirstHand.prio < typeOfSecondHand.prio) return -1
        return compareHighCards(firstHand, secondHand)
    }

    fun winsWithJoker(firstHand: String, secondHand: String): Int {
        val typeOfFirstHand = getTypeWithJoker(firstHand)
        val typeOfSecondHand = getTypeWithJoker(secondHand)
        if (typeOfFirstHand.prio > typeOfSecondHand.prio) return 1
        if (typeOfFirstHand.prio < typeOfSecondHand.prio) return -1
        return compareHighCardsWithJoker(firstHand, secondHand)
    }

    fun part1(input: List<String>): Int {
        val handBids = input.map { line ->
            val (hand, bid) = line.split(" ")
            hand to bid.toInt()
        }
        val sortedHandBids = handBids.sortedWith(Comparator { (firstHand, _), (secondHand, _) ->
            return@Comparator wins(firstHand, secondHand)
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
            return@Comparator winsWithJoker(firstHand, secondHand)
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
