package com.nexdev.smartroller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.nexdev.smartroller.adapter.ItemAdapter
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration.*
import com.nexdev.smartroller.adapter.RollItemAdapter
import com.nexdev.smartroller.data.Datasource
import com.nexdev.smartroller.data.RollItemDatasource
import com.nexdev.smartroller.databinding.ActivityMainBinding
import com.nexdev.smartroller.model.Item
import com.nexdev.smartroller.model.RollItem
import java.util.LinkedList


open class MainActivity : AppCompatActivity() {
    companion object {
        var dice = DiceX() // declare and initialize dice object that will be used in main activity
        var currentToast: Toast? = null
    }

    private lateinit var binding: ActivityMainBinding // declare main activity binding

    // declare main activity recyclerview variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var myDataset: MutableList<Item>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val topToolbar: Toolbar = findViewById(R.id.action_bar)
        setSupportActionBar(topToolbar)

        // initialize main activity recyclerview
        myDataset = Datasource().loadItems()
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, myDataset)
        recyclerView.setHasFixedSize(true)

        // initialize buttons in main activity
        findViewById<Button>(R.id.singlePair).setOnClickListener { outputSinglePair() }
        findViewById<Button>(R.id.doublePair).setOnClickListener { outputDoublePair() }
        findViewById<Button>(R.id.clearButton).setOnClickListener { resetStats() }
        findViewById<Button>(R.id.history_button).setOnClickListener { launchHistory() }

        MobileAds.initialize(this) {}

        val requestConfiguration = MobileAds.getRequestConfiguration()
        val updatedRequestConfiguration = requestConfiguration.toBuilder()
            .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
            .setTagForUnderAgeOfConsent(TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
            .build()

        MobileAds.setRequestConfiguration(updatedRequestConfiguration)

        loadBanner() // load the banner ad at the end
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
        showToast("Rolled one pair")
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
        showToast("Rolled two pairs")
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
        showToast("Dice reset")
    }

    /*private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = findViewById<AdView>(R.id.banner_ad).width.toFloat()
            if(adWidthPixels == 0f)
                adWidthPixels = outMetrics.widthPixels.toFloat()

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }*/
    private fun loadBanner() {
        // Create ad view
        val adView = findViewById<AdView>(R.id.banner_ad)
        //adView.setAdSize(adSize)
        //adView.adUnitId = "ca-app-pub-3940256099942544~3347511713"

        // Create ad request
        val adRequest = AdRequest.Builder().build()

        // Start loading Ad in background
        adView.loadAd(adRequest)
    }

    private fun showToast(message: String?) {
        currentToast?.cancel()

        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}