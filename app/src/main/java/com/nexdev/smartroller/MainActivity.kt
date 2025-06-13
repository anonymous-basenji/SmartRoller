package com.nexdev.smartroller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.nexdev.smartroller.adapter.ItemAdapter
import com.nexdev.smartroller.data.Datasource
import com.nexdev.smartroller.data.RollItemDatasource
import com.nexdev.smartroller.databinding.ActivityMainBinding
import com.nexdev.smartroller.model.Item
import com.nexdev.smartroller.model.RollItem

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


open class MainActivity : AppCompatActivity() {
    companion object {
        var dice = DiceX() // declare and initialize dice object that will be used in main activity
    }

    private var currentToast: Toast? = null // declare current toast variable
    private var adView: AdView? = null // declare adview variable
    private lateinit var binding: ActivityMainBinding // declare main activity binding

    // declare main activity recyclerview variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var myDataset: MutableList<Item>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topToolbar: Toolbar = findViewById(R.id.actionBar)
        setSupportActionBar(topToolbar)

        // initialize main activity recyclerview
        myDataset = Datasource().loadItems()
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, myDataset)
        recyclerView.setHasFixedSize(true)

        // initialize buttons in main activity
        findViewById<Button>(R.id.singlePair).setOnClickListener{ outputSinglePair() }
        findViewById<Button>(R.id.doublePair).setOnClickListener{ outputDoublePair() }
        findViewById<Button>(R.id.clearButton).setOnClickListener{ resetStats() }
        findViewById<Button>(R.id.history_button).setOnClickListener { launchHistory() }

        MobileAds.initialize(this) {}
        adView = findViewById(R.id.ad_view_container)

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    private fun launchHistory() {
        intent = Intent(this, HistoryActivity::class.java) // create intent to launch history activity
        startActivity(intent) // start intent
    }

    @SuppressLint("ResourceType", "NotifyDataSetChanged")
    private fun outputSinglePair() {
        val vibe: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator // create vibrator
        vibe.vibrate(30) // vibrate for 30 ms

        val currentPair = dice.roll() //generates pair
        myDataset[0].header = "$currentPair" //updates number area to display current pair
        myDataset[0].body = dice.displayCount() //updates count area to display current pair count

        recyclerView.adapter?.notifyItemChanged(0) //notifies RecyclerView number/count area changed

        myDataset[1].body = dice.returnResults() //updates stats area

        recyclerView.adapter?.notifyItemChanged(1) //notifies RecyclerView stats area changed

        RollItemDatasource.add(RollItem(currentPair, 0))

        showToast("Single pair rolled!")
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }

    @SuppressLint("ResourceType", "NotifyDataSetChanged")
    private fun outputDoublePair() {
        //vibrates for 30ms
        val vibe: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibe.vibrate(30)

        val currentFirstPair = dice.roll() //generates first pair
        val currentSecondPair = dice.roll() //generates second pair

        myDataset[0].header = "$currentFirstPair, $currentSecondPair" //updates number region to display the two pairs
        myDataset[0].body = dice.displayCount() //updates the count region to display the current pair count

        recyclerView.adapter?.notifyItemChanged(0) //notifies RecyclerView number/count area changed

        myDataset[1].body = dice.returnResults() //updates the stats area

        recyclerView.adapter?.notifyItemChanged(1) //notifies RecyclerView that stats area changed

        RollItemDatasource.add(RollItem(currentFirstPair, currentSecondPair))
        showToast("Double pair rolled!")
    }

    @SuppressLint("ResourceType", "NotifyDataSetChanged")
    private fun resetStats() {
        //vibrates for 30ms
        val vibe: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibe.vibrate(30)

        dice.clear() //resets dice stats

        myDataset[0].header = "0" //resets number area to default "0"
        myDataset[0].body = dice.displayCount() // refreshes pair count area to display 0 pairs rolled

        recyclerView.adapter?.notifyItemChanged(0) //notifies RecyclerView number/count area changed

        myDataset[1].body = dice.returnResults() //refreshes stats area

        recyclerView.adapter?.notifyItemChanged(1) //notifies RecyclerView stats area changed

        RollItemDatasource.clear() // clears roll item datasource for history recyclerview
    }

    private fun showToast(message: String) {
        currentToast?.cancel()

        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}