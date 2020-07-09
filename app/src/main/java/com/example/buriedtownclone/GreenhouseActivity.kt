package com.example.buriedtownclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.buriedtownclone.homeequipment.Greenhouse
import kotlinx.android.synthetic.main.activity_greenhouse.*
import kotlin.math.round
import kotlin.math.roundToInt

class GreenhouseActivity : AppCompatActivity() {

    private var harvestReady = false
    private var databaseTimestamp = getTimestampFromDatabase()
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greenhouse)

        VisualsUpdater.activity = this
        GameHandler.context = this
        TimeHandler.context = this
        showStatsBarFragment()
        setLayoutViews()
        if(!harvestReady)
            startRefreshingJob()
    }

    override fun onBackPressed() {
        countDownTimer?.cancel()
        super.onBackPressed()
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(),"stats bar")
            .commit()
    }

    private fun setLayoutViews(timeRemaining: String  = getTimeLeftFromDatabase()){
        setTimeRemainingText(timeRemaining)
        enableHarvestButtonIfReady()
    }

    private fun setTimeRemainingText(timeRemaining: String){
        timeRemainingText.text = timeRemaining
    }

    private fun enableHarvestButtonIfReady(){
        if(harvestReady){
            harvestCrop.isEnabled = true
        }
    }

    private fun getTimeLeftFromDatabase(): String{
        val minutesLeft = getMinutesLeftFromTimestamp()
        return if(minutesLeft > 0){
            "${minutesLeft.toInt()} Minutes Remaining"
        } else{
            val secondsRemaining = getSecondsLeftFromTimestamp()
            if(secondsRemaining > 0)
                "${secondsRemaining.toInt()} Seconds Remaining"
            else{
                "Ready to be harvested"
            }
        }
    }

    private fun getMinutesLeftFromTimestamp(): Long{
        return round((databaseTimestamp - System.currentTimeMillis()) / (1000*60f)).toLong()
    }

    private fun getSecondsLeftFromTimestamp(): Long{
        val differenceInSeconds = round((databaseTimestamp - System.currentTimeMillis()) / 1000f).toLong()

        return if(differenceInSeconds > 0){
            differenceInSeconds
        } else{
            harvestReady = true
            0
        }
    }

    private fun getTimestampFromDatabase(): Long{
        return Database.getTimestamp("greenhouse")
    }

    fun harvestButtonClicked(view: View){
        if(Inventory.addItem(Apple())){
            replant()
        }
        else{
            Toast.makeText(this, "No space in you inventory", Toast.LENGTH_LONG).show()
        }
    }

    private fun replant(){
        harvestCrop.isEnabled = false
        harvestReady = false
        updateTimestamp()
        setLayoutViews()
        Database.setInventory(Player.getInventory().itemsInside)
        setLayoutViews()
        startRefreshingJob()
    }

    private fun updateTimestamp(){
        val currentTime = System.currentTimeMillis()
        val timeToStore = currentTime + (1/Greenhouse.level) * 600_000
        Database.updateTimestamp("greenhouse", timeToStore)
        databaseTimestamp = timeToStore
    }

    private fun startRefreshingJob(){
        val timeToCount = (getSecondsLeftFromTimestamp()*1000)
        countDownTimer = object: CountDownTimer(timeToCount, 1000){
            override fun onTick(millisUntilFinished: Long) {
                val timeStringToShow = convertCountDownMillisToSecOrMin(millisUntilFinished)
                refreshLayout(timeStringToShow)
            }

            override fun onFinish() {
                harvestReady = true
                refreshLayout("Ready to be harvested")
            }
        }
        countDownTimer?.start()
    }

    private fun convertCountDownMillisToSecOrMin(millisUntilFinished: Long): String{
        return if(round(millisUntilFinished / 1000f) < 60){
            "${round(millisUntilFinished / 1000f).toInt()} Seconds Remaining"
        } else{
            "${round((millisUntilFinished / (1000*60f))).toInt()} Minutes Remaining"
        }
    }

    private fun refreshLayout(timeRemaining: String){
        setLayoutViews(timeRemaining)
    }
}