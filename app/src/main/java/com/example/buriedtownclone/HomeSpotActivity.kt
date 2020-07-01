package com.example.buriedtownclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeSpotActivity : AppCompatActivity() {

    var database = Database()
    var player = Player()
    private lateinit var homeEquipmentFragment: HomeEquipmentFragment
   // var visualsUpdater =  VisualsUpdater()
    //var gameHandler = GameHandler()

    override fun onBackPressed() {
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_spot)

        VisualsUpdater.activity = this
        GameHandler.context = this
        TimeHandler.context = this

        manageSpot()
        showStatsBarFragment()
        showHomeEquipmentFragment()
    }

    private fun manageSpot(){
        val currentSpot = getClickedSpotObject()
        setSpotAsVisited(currentSpot)
    }
    private fun getClickedSpotObject(): Spot{
        return intent.getSerializableExtra(Definitions.spotItems) as Spot
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(player),"stats bar")
            .commit()
    }

    private fun setSpotAsVisited(spot: Spot){
        spot.visited()
    }

    private fun showHomeEquipmentFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.equipmentContainer, HomeEquipmentFragment(),"home")
            .commit()
    }
}