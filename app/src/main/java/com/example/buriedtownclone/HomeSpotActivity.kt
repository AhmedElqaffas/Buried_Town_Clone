package com.example.buriedtownclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeSpotActivity : AppCompatActivity() {

    var database = Database()
    var player = Player()

    override fun onBackPressed(){
        val homeEquipmentFragment = getEquipmentFragment()
        homeEquipmentFragment.saveEquipment()
        //setResult(RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_spot)
    }

    override fun onResume() {
        super.onResume()
        updateClassesContext()
        manageSpot()
        showStatsBarFragment()
        showHomeEquipmentFragment()
    }

    private fun updateClassesContext(){
        VisualsUpdater.activity = this
        GameHandler.context = this
        TimeHandler.context = this
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

    private fun getEquipmentFragment(): HomeEquipmentFragment{
        return supportFragmentManager.findFragmentByTag("home") as HomeEquipmentFragment
    }
}