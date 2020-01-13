package aoc2019

import java.io.File
import java.lang.IllegalStateException

fun main() {
    val rules = File("./data/day14.txt").readLines()

    val result1 = day14_1(rules)
    println(result1)

    val result2 = day14_2(rules)
    println(result2)
}

private const val FUEL = "FUEL"
private const val SRC = "ORE"
private val MAX_SRC = 1000000000000L

fun day14_1(rulesStr: List<String>): Long {
    val rules = parseRules(rulesStr)

    return calc(rules, 1L)
}

fun day14_2(rulesStr: List<String>): Long {
    val rules = parseRules(rulesStr)

    var a = 0L
    var b = Int.MAX_VALUE.toLong()

    //if b is still not enough
    while (calc(rules, b) < MAX_SRC) {
        a = b
        b *= 2
    }

    //use binary search
    while ((b - a) > 1) {
        val middle = a + (b - a) / 2
        if (calc(rules, middle) > MAX_SRC) {
            b = middle
        } else {
            a = middle
        }
    }

    return a
}

private fun parseRules(rulesStr: List<String>): Map<String, Rule> {
    return rulesStr.associate { ruleStr ->
        val itemsStr = ruleStr.substringBefore("=>").trim().split(",").map(String::trim)
        val items = itemsStr.map { itemStr ->
            Item(itemStr.substringAfter(" ").trim(), itemStr.substringBefore(" ").trim().toLong())
        }

        val res = ruleStr.substringAfter("=>").trim()
        val name = res.substringAfter(" ").trim()
        val count = res.substringBefore(" ").trim().toLong()

        name to Rule(count, items)
    }
}

private fun calc(rules: Map<String, Rule>, count: Long): Long {
    var srcCount = 0L
    val produce = mutableMapOf<String, Long>()
    val reserve = mutableMapOf<String, Long>()
    produce[FUEL] = count

    while(produce.isNotEmpty()) {
        val itemName = produce.keys.first()

        var count = produce.remove(itemName) ?: throw IllegalStateException("name $itemName not found")
        if (itemName == SRC) {
            srcCount += count
            continue
        }

        val reserveCount = reserve[itemName] ?: 0

        if (reserveCount >= count) {
            reserve[itemName] = reserveCount - count
            continue
        }

        count -= reserveCount
        val rule = rules[itemName] ?: throw IllegalStateException("Rule $itemName not found")

        //count how many portions need to do
        val portions = (count / rule.count) + if ((count % rule.count) > 0) 1 else 0

        rule.items.forEach {
            val totalCount = it.count * portions
            val fromReserve = reserve[it.name] ?: 0

            if (fromReserve >= totalCount) {
                reserve[it.name] = fromReserve - totalCount
            } else {
                produce[it.name] = produce.getOrDefault(it.name, 0) + (totalCount - fromReserve)
                reserve.remove(it.name)
            }
        }

        //save the rest to the reserve
        if (rule.count * portions > count) {
            reserve[itemName] = rule.count * portions - count
        }
    }

    return srcCount
}

private data class Item(val name: String, val count: Long)
private data class Rule(val count: Long, val items: List<Item>)