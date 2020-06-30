package com.example.buriedtownclone

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SpotActivity : AppCompatActivity(), ItemsFragment.ItemActionDecider {

    private var database = Database()
    var player =  Player()
    private var visualsUpdater =  VisualsUpdater()
    private lateinit var spotItemsFragment: ItemsFragment
    private lateinit var inventoryItemsFragment: ItemsFragment
    private lateinit var inventoryHelperFragment: InventoryHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot)

        VisualsUpdater.activity = this
        GameHandler.context = this
        TimeHandler.context = this
        showStatsBarFragment()
        manageSpot()
        showInventoryHelperFragment()

        val handler = Handler()
        handler.postDelayed({
            setupCommunicationBetweenActivityAndFragments()
        }, 10)

    }

    override fun onBackPressed() {
        intent.putExtra("spot", getClickedSpotObject())
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(player),"stats bar")
            .commit()
    }

    private fun manageSpot(){
        val currentSpot = getClickedSpotObject()
        showSpotItemsFragment(currentSpot)
        showInventoryFragment(player.getInventory())

        setSpotAsVisited(currentSpot)
    }

    private fun getClickedSpotObject(): Spot{
        return intent.getSerializableExtra("spot") as Spot
    }

    private fun showSpotItemsFragment(container: ItemsContainer){
        supportFragmentManager.beginTransaction().replace(R.id.spotItemsContainer,
            ItemsFragment(container), "spot")
            .commit()
    }
    private fun showInventoryFragment(container: ItemsContainer){
        supportFragmentManager.beginTransaction().replace(R.id.inventoryContainer,
            ItemsFragment(container), "inventory")
            .commit()
    }

    private fun setSpotAsVisited(spot: Spot){
        spot.visited()
    }

    private fun showInventoryHelperFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.inventoryHelperContainer,
            InventoryHelper(), "inventory helper").commit()
    }

    private fun setupCommunicationBetweenActivityAndFragments(){
        initializeFragments()
    }

    private fun initializeFragments(){
        spotItemsFragment = supportFragmentManager.findFragmentByTag("spot")!! as ItemsFragment
        inventoryItemsFragment = supportFragmentManager.findFragmentByTag("inventory")!! as ItemsFragment
        inventoryHelperFragment = supportFragmentManager.findFragmentByTag("inventory helper")!! as InventoryHelper

    }

    override fun onItemClicked(item: Item, slotsFound: Int, itemsFragment: ItemsFragment) {
        if(shouldConsumeItem()){
            if(itemsFragment.tag == "spot"){
                spotItemsFragment.consumeItem(item,slotsFound,player)
                updateSpotItemsInDatabase()
            }
            else{
                inventoryItemsFragment.consumeItem(item,slotsFound,player)
                commitInventoryChangesToDatabase()
            }
            visualsUpdater.showStatsInStatsBar(player)
        }
        else{ // should swap item from spot to inventory and vice versa
            if(itemsFragment.tag == "spot" && existsSlotForThisItem(inventoryItemsFragment ,item)){
                spotItemsFragment.removeItem(slotsFound)
                commitInventoryChangesToDatabase()
                updateSpotItemsInDatabase()
            }
            else{
                if(existsSlotForThisItem(spotItemsFragment, item)){
                    inventoryItemsFragment.removeItem(slotsFound)
                    commitInventoryChangesToDatabase()
                    updateSpotItemsInDatabase()
                }
            }
        }
    }

    private fun shouldConsumeItem(): Boolean {
        return inventoryHelperFragment.isSwitchSetToConsume()
    }

    private fun existsSlotForThisItem(container: ItemsFragment, item: Item): Boolean{
        return container.addItem(item)
    }

    private fun commitInventoryChangesToDatabase(){
        database.setInventory(inventoryItemsFragment.getItemsMap())
    }

    private fun updateSpotItemsInDatabase(){
        database.updateSpotItems(getClickedSpotObject())
    }
}