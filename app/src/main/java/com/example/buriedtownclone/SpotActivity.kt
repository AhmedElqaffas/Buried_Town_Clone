package com.example.buriedtownclone

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SpotActivity : AppCompatActivity() {

    lateinit var player: Player
    var visualsUpdater = VisualsUpdater(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot)

        createUpdatedPlayerObject()

        manageSpot()

        showStatsBarFragment()
    }

    override fun onBackPressed() {
        intent.putExtra("spot", getClickedSpotObject())
        intent.putExtra("inventory", player.getInventory())
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun createUpdatedPlayerObject(){
        player = Player(this)
        player.updateStatsFromDatabase()
    }

    private fun manageSpot(){
        var currentSpot = getClickedSpotObject()
        showSpotItemsFragment(currentSpot)
        showInventoryFragment(player.getInventory())

        setSpotAsVisited(currentSpot)
    }

    private fun getClickedSpotObject(): Spot{
        return intent.getSerializableExtra("spot") as Spot
    }

    private fun showSpotItemsFragment(container: ItemsContainer){
        supportFragmentManager.beginTransaction().replace(R.id.spotItemsContainer,
            ItemsFragment(container), container::class.java.simpleName.toLowerCase())
            .commit()
    }
    private fun showInventoryFragment(container: ItemsContainer){
        supportFragmentManager.beginTransaction().replace(R.id.inventoryContainer,
            ItemsFragment(container), container::class.java.simpleName.toLowerCase())
            .commit()
    }

    private fun setSpotAsVisited(spot: Spot){
        spot.visited()
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(player),"stats bar")
            .commit()
    }
}