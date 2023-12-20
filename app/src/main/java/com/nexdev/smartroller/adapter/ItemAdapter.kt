/*
 * Source for adapter: https://blog.mindorks.com/recyclerview-multiple-view-types-in-android/
 */
//test change
package com.nexdev.smartroller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nexdev.smartroller.R
import com.nexdev.smartroller.model.Item

class ItemAdapter(private val context: Context, var list: List<Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private inner class View1ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var header: TextView = itemView.findViewById(R.id.dice)
        var body: TextView = itemView.findViewById(R.id.count)
        fun bind(position: Int) {
            val item = list[position]
            header.text = item.header
            body.text = item.body
        }
    }

    private inner class View2ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var header: TextView = itemView.findViewById(R.id.hist_header)
        var body: TextView = itemView.findViewById(R.id.hist_body)
        fun bind(position: Int) {
            val item = list[position]
            header.text = item.header
            body.text = item.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return View1ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.roll_item, parent, false)
            )
        }
        return View2ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.stats_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType == VIEW_TYPE_ONE) {
            (holder as View1ViewHolder).bind(position)
        } else {
            (holder as View2ViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}