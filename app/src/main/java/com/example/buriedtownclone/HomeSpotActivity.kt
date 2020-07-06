package com.example.buriedtownclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home_spot.*

class HomeSpotActivity : AppCompatActivity() {

    override fun onBackPressed(){
        val homeEquipmentFragment = getEquipmentFragment()
        setResult(RESULT_OK, intent)
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
        manageFirstVisit(currentSpot)
    }
    private fun getClickedSpotObject(): Spot{
        return intent.getSerializableExtra(Definitions.spotItems) as Spot
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(),"stats bar")
            .commit()
    }

    private fun manageFirstVisit(spot: Spot){
        if(!spot.visited){
            addPistolToInventory()
            spot.visited()
            VisualsUpdater.showFirstHomeVisitDialog(homeSpotRootLayout, horizontalGuideline_endEquipment)
        }
    }

    private fun addPistolToInventory(){
        Player.addToInventory(Pistol(), "1")
    }

    private fun showHomeEquipmentFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.equipmentContainer, HomeEquipmentsFragment(),"home")
            .commit()
    }

    private fun getEquipmentFragment(): HomeEquipmentsFragment{
        return supportFragmentManager.findFragmentByTag("home") as HomeEquipmentsFragment
    }
}