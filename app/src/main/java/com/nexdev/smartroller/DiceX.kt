package com.nexdev.smartroller

class DiceX {
    private var dice = 0
    private var prob = 0.0
    private var totalRolls = 0
    private var pastRolls = mutableMapOf(
        2 to 0,
        3 to 0,
        4 to 0,
        5 to 0,
        6 to 0,
        7 to 0,
        8 to 0,
        9 to 0,
        10 to 0,
        11 to 0,
        12 to 0
    )

    fun roll(): Int {
        prob = Math.random()
        dice = if (prob < 2.778 / 100) {
            2
        } else if (prob < 8.333 / 100) {
            3
        } else if (prob < 16.667 / 100) {
            4
        } else if (prob < 27.778 / 100) {
            5
        } else if (prob < 41.667 / 100) {
            6
        } else if (prob < 58.333 / 100) {
            7
        } else if (prob < 72.222 / 100) {
            8
        } else if (prob < 83.333 / 100) {
            9
        } else if (prob < 91.667 / 100) {
            10
        } else if (prob < 97.222 / 100) {
            11
        } else {
            12
        }
        
        pastRolls[dice] = pastRolls[dice]!! + 1
        updateCount()
        return dice
    }

    fun returnResults(): String {
        var result = ""

        result += String.format("%-6s%-10s%-10s%n", "Pair", "% rolled", "# rolled")

        for(i in 2..12) {
            val numRolls = pastRolls[i]!!
            val percentage = (numRolls.toDouble() / totalRolls) * 100.0
            var strPercentage = String.format("%.2f", percentage)
            strPercentage += "%"

            if(percentage.isNaN())
                strPercentage = "0.00%"

            result += String.format("%-6d%-10s%-10d%n", i, strPercentage, numRolls)
        }

        return result
    }

    fun clear() {
        for(i in 2..12) {
            pastRolls[i] = 0
        }

        updateCount()
    }

    private fun updateCount() {
        totalRolls = 0
        for(i in 2..12) {
            totalRolls += pastRolls[i]!!
        }
    }

    fun displayCount(): String {
        return "Total pairs rolled: $totalRolls"
    }
}