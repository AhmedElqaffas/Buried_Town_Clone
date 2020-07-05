package com.example.buriedtownclone

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SpotActivity : AppCompatActivity(), ItemsFragment.ItemActionDecider {

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
        intent.putExtra(Definitions.spotItems, getClickedSpotObject())
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(),"stats bar")
            .commit()
    }

    private fun manageSpot(){
        val currentSpot = getClickedSpotObject()
        showSpotItemsFragment(currentSpot)
        showInventoryFragment(Player.getInventory())

        setSpotAsVisited(currentSpot)
    }

    private fun getClickedSpotObject(): Spot{
        return intent.getSerializableExtra(Definitions.spotItems) as Spot
    }

    private fun showSpotItemsFragment(container: ItemsContainer){
        supportFragmentManager.beginTransaction().replace(R.id.spotItemsContainer,
            ItemsFragment(container), Definitions.spotItems)
            .commit()
    }
    private fun showInventoryFragment(container: ItemsContainer){
        supportFragmentManager.beginTransaction().replace(R.id.inventoryContainer,
            ItemsFragment(container), Definitions.inventoryItems)
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
        spotItemsFragment = supportFragmentManager.findFragmentByTag(Definitions.spotItems)!! as ItemsFragment
        inventoryItemsFragment = supportFragmentManager.findFragmentByTag(Definitions.inventoryItems)!! as ItemsFragment
        inventoryHelperFragment = supportFragmentManager.findFragmentByTag("inventory helper")!! as InventoryHelper

    }

    override fun onItemClicked(item: Item, slotsFound: Int, itemsFragment: ItemsFragment) {
        if(shouldConsumeItem()){
            if(itemsFragment.tag == Definitions.spotItems){
                spotItemsFragment.consumeItem(item,slotsFound)
                updateSpotItemsInDatabase()
            }
            else{
                inventoryItemsFragment.consumeItem(item,slotsFound)
                commitInventoryChangesToDatabase()
            }
            //visualsUpdater.showStatsInStatsBar(player)
        }
        else{ // should swap item from spot to inventory and vice versa
            if(itemsFragment.tag == Definitions.spotItems && existsSlotForThisItem(inventoryItemsFragment ,item)){
                spotItemsFragment.removeItem(slotsFound)
                commitInventoryChangesToDatabase()
                updateSpotItemsInDatabase()
            }
            else if(itemsFragment.tag == Definitions.inventoryItems && existsSlotForThisItem(spotItemsFragment ,item)){
                inventoryItemsFragment.removeItem(slotsFound)
                commitInventoryChangesToDatabase()
                updateSpotItemsInDatabase()
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
        Database.setInventory(inventoryItemsFragment.getItemsMap())
    }

    private fun updateSpotItemsInDatabase(){
        Database.updateSpotItems(getClickedSpotObject())
    }
}