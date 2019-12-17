package aoc2019

import java.io.File
import java.lang.IllegalStateException
import java.math.BigInteger

fun main() {
    val rules = File("./data/day14.txt").readLines()

    val result1 = day14_1(rules)
    println(result1)

    val result2 = day14_2(rules)
    println(result2)
}

private const val FUEL = "FUEL"
private const val SRC = "ORE"
private val MAX_SRC = BigInteger("1000000000000")

fun day14_1(rulesStr: List<String>): Int {
    val rules = parseRules(rulesStr)

    return calc(rules, mutableMapOf())
}

fun day14_2(rulesStr: List<String>): Int {
    val rules = parseRules(rulesStr)
    val reserve = mutableMapOf<String, Int>()

    var remain = MAX_SRC
    var fuel = 0

    do {
        val used = calc(rules, reserve).toBigInteger()
        if (used <= remain) {
            fuel++
        }
        remain -= used
    } while(remain > BigInteger.ZERO)

    return fuel
}

private fun parseRules(rulesStr: List<String>): Map<String, Rule> {
    return rulesStr.associate { ruleStr ->
        val itemsStr = ruleStr.substringBefore("=>").trim().split(",").map(String::trim)
        val items = itemsStr.map { itemStr ->
            Item(itemStr.substringAfter(" ").trim(), itemStr.substringBefore(" ").trim().toInt())
        }

        val res = ruleStr.substringAfter("=>").trim()
        val name = res.substringAfter(" ").trim()
        val count = res.substringBefore(" ").trim().toInt()

        name to Rule(count, items)
    }
}

private fun calc(rules: Map<String, Rule>, reserve: MutableMap<String, Int>): Int {
    var srcCount = 0
    val produce = mutableMapOf<String, Int>()
    produce[FUEL] = 1

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

private data class Item(val name: String, val count: Int)
private data class Rule(val count: Int, val items: List<Item>)