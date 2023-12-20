package com.nexdev.smartroller.data

import com.nexdev.smartroller.model.RollItem
import java.util.LinkedList

class RollItemDatasource {
    companion object {
        private val list = LinkedList<RollItem>()

        fun loadItems(): LinkedList<RollItem> {
            return list
        }

        fun add(roll: RollItem) {
            list.addFirst(roll)
        }

        fun clear() {
            list.clear()
        }
    }
}