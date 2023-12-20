package com.nexdev.smartroller

import java.util.LinkedList

class RollList {
    private var pastRolls = LinkedList<Roll>()

    fun add(roll: Roll) {
        pastRolls.add(roll)
        if(pastRolls.size > 100) {
            pastRolls.removeAt(0)
        }
    }
}