package com.nexdev.smartroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nexdev.smartroller.adapter.RollItemAdapter
import com.nexdev.smartroller.data.RollItemDatasource
import com.nexdev.smartroller.databinding.ActivityHistoryBinding
import com.nexdev.smartroller.model.RollItem
import java.util.LinkedList

open class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    protected lateinit var historyDataset: LinkedList<RollItem>
    protected lateinit var historyRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyDataset = RollItemDatasource.loadItems()
        historyRecyclerView = findViewById(R.id.history_recycler_view)
        historyRecyclerView.adapter = RollItemAdapter(this, historyDataset)
        historyRecyclerView.setHasFixedSize(false)

        findViewById<Button>(R.id.back_button).setOnClickListener { back() }

        findViewById<Button>(R.id.info_button).setOnClickListener {
            val snackBar = Snackbar.make(it, "Most recent rolls are at the top.", Snackbar.LENGTH_LONG)
            snackBar.show()
        }
    }

    fun back() {
        this.finish()
    }
}