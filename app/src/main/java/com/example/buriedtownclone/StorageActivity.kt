package com.example.buriedtownclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.buriedtownclone.homeequipment.Storage

class StorageActivity : AppCompatActivity(), ItemsFragment.ItemActionDecider {

    private lateinit var spotItemsFragment: ItemsFragment
    private lateinit var inventoryItemsFragment: ItemsFragment
    private lateinit var inventoryHelperFragment: InventoryHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        VisualsUpdater.activity = this
        GameHandler.context = this
        TimeHandler.context = this

        showStatsBarFragment()
        showContainersFragments()
        showInventoryHelperFragment()

        val handler = Handler()
        handler.postDelayed({
            setupCommunicationBetweenActivityAndFragments()
        }, 10)

    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(),"stats bar")
            .commit()
    }

    private fun showInventoryHelperFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.inventoryHelperContainer,
            InventoryHelper(), "inventory helper").commit()
    }

    private fun showContainersFragments(){
        showStorageItemsFragment()
        showInventoryFragment(Player.getInventory())
    }

    private fun showStorageItemsFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.spotItemsContainer,
            ItemsFragment(HomeSpot), Definitions.spotItems)
            .commit()
    }
    private fun showInventoryFragment(container: ItemsContainer){
        supportFragmentManager.beginTransaction().replace(R.id.inventoryContainer,
            ItemsFragment(container), Definitions.inventoryItems)
            .commit()
    }

    private fun setupCommunicationBetweenActivityAndFragments(){
        initializeFragments()
    }

    private fun initializeFragments(){
        spotItemsFragment = supportFragmentManager.findFragmentByTag(Definitions.spotItems) as ItemsFragment
        inventoryItemsFragment = supportFragmentManager.findFragmentByTag(Definitions.inventoryItems) as ItemsFragment
        inventoryHelperFragment = supportFragmentManager.findFragmentByTag("inventory helper") as InventoryHelper
    }

    override fun onItemClicked(item: Item, slotsFound: Int, itemsFragment: ItemsFragment) {
        if(shouldConsumeItem()){
            if(itemsFragment.tag == Definitions.spotItems){
                spotItemsFragment.consumeItem(item,slotsFound)
                HomeSpot.updateEquipment(Storage)
            }
            else{
                inventoryItemsFragment.consumeItem(item,slotsFound)
                commitInventoryChangesToDatabase()
            }
        }
        else{ // should swap item from storage to inventory and vice versa
            if(itemsFragment.tag == Definitions.spotItems && existsSlotForThisItem(inventoryItemsFragment ,item)){
                spotItemsFragment.removeItem(slotsFound)
                commitInventoryChangesToDatabase()
                HomeSpot.updateEquipment(Storage)
            }
            else if(itemsFragment.tag == Definitions.inventoryItems && existsSlotForThisItem(spotItemsFragment ,item)){
                inventoryItemsFragment.removeItem(slotsFound)
                commitInventoryChangesToDatabase()
                HomeSpot.updateEquipment(Storage)
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
}