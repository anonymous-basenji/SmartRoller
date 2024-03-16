/*
 * Source for adapter: https://blog.mindorks.com/recyclerview-multiple-view-types-in-android/
 */

package com.nexdev.smartroller.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nexdev.smartroller.R
import com.nexdev.smartroller.model.RollItem
import java.util.LinkedList

class RollItemAdapter(private val context: Context, var list: MutableList<RollItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class RollItemViewHolder(rollItemView: View) :
        RecyclerView.ViewHolder(rollItemView) {
        val rollInHistory: TextView? = rollItemView.findViewById(R.id.roll_item_in_history)
        fun bind(position: Int) {
            val rollItem = list[position]
            rollInHistory!!.text = "$rollItem"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RollItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.roll_item_list_in_history, parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRoll(roll: RollItem) {
        if(list.size > 100) {
            list.removeAt(0)
            notifyDataSetChanged()
        }

        list.add(roll)

        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RollItemViewHolder).bind(position)
    }
}