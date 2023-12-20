package com.nexdev.smartroller.data

import com.nexdev.smartroller.DiceX
import com.nexdev.smartroller.model.Item

class Datasource {
    fun loadItems(): MutableList<Item> {
        val dice = DiceX()
        return mutableListOf<Item>(
            Item("0", "Total pairs rolled: 0", 1),
            Item("Stats", dice.returnResults(), 2)
        )
    }
}