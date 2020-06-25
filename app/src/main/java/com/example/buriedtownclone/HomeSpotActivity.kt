package com.example.buriedtownclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeSpotActivity : AppCompatActivity() {

    private var database = Database(this)
    lateinit var player: Player
    var visualsUpdater =  VisualsUpdater(this)

    override fun onBackPressed() {
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_spot)

        createUpdatedPlayerObject()
        manageSpot()
        showStatsBarFragment()
    }

    private fun createUpdatedPlayerObject(){
        player = Player(this)
        player.updateStatsFromDatabase()
    }

    private fun manageSpot(){
        var currentSpot = getClickedSpotObject()
        setSpotAsVisited(currentSpot)
    }
    private fun getClickedSpotObject(): Spot{
        return intent.getSerializableExtra("spot") as Spot
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(player),"stats bar")
            .commit()
    }

    private fun setSpotAsVisited(spot: Spot){
        spot.visited()
    }
}