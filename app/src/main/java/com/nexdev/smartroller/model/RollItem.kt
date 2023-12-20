package com.nexdev.smartroller.model

import androidx.appcompat.app.AppCompatActivity

data class RollItem(val firstRoll: Int, val secondRoll: Int): AppCompatActivity() {
    override fun toString(): String {
        if(secondRoll == 0) {
            return "$firstRoll"
        }
        return "$firstRoll, $secondRoll"
    }
}