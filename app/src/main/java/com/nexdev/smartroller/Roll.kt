package com.nexdev.smartroller

class Roll(private val firstPair: Int, private val secondPair: Int) {
    override fun toString(): String {
        if(secondPair == 0) {
            return "$firstPair"
        }
        return "$firstPair, $secondPair"
    }
}