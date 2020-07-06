package com.example.buriedtownclone

import com.google.common.collect.LinkedListMultimap

object Player{

    private var hp: Int = 100
    private var hunger:Int = 100
    private var thirst: Int = 100
    private var locationX = 0
    private var locationY = 0
    private var inventory: Inventory = Inventory()


    fun updateStatsFromDatabase(){

        this.setHealthPoints(Database.getHealthPoints()!!)
        hunger = Database.getHunger()
        thirst = Database.getThirst()
        inventory.itemsInside = Database.getInventory()
        val location = Database.getPlayerLocation()
        locationX = location[0]
        locationY = location[1]
    }

   private fun setHealthPoints(value: Int){
        if(value == 0) {
            GameHandler.endGame()
        }
        else{
            hp = value
        }
    }
    fun setLocation(x: Int, y: Int){
        locationX = x
        locationY = y
        updateLocationInDatabase()
    }
    private fun updateLocationInDatabase(){
        Database.updatePlayerLocation(locationX, locationY)
    }

    fun getHunger(): Int{
        return hunger
    }
    fun getThirst(): Int{
        return thirst
    }
    fun getHealthPoints(): Int{
        return hp
    }
    fun getLocationX(): Int{
        return locationX
    }
    fun getLocationY(): Int{
        return locationY
    }
    fun getInventory(): Inventory{
        return inventory
    }

    fun updateHP(hpBonus: Int){
        hp += hpBonus
        if(hp > 100)
            hp = 100
        else if(hp <= 0)
            GameHandler.endGame()
        VisualsUpdater.showStatsInStatsBar()
        Database.setHP(hp)
    }

    fun updateHunger(hungerBonus: Int){
        hunger += hungerBonus
        if(hunger > 100)
            hunger = 100
        else if(hunger <= 0) {
            GameHandler.endGame()
        }
        VisualsUpdater.showStatsInStatsBar()
        Database.setHunger(hunger)
    }
    fun updateThirst(thirstBonus: Int){
        thirst += thirstBonus
        if(thirst > 100)
            thirst = 100
        if(thirst <= 0) {
            GameHandler.endGame()
        }
        VisualsUpdater.showStatsInStatsBar()
        Database.setThirst(thirst)
    }

    fun addToInventory(item: Item, quantity: String){
        inventory.itemsInside.put(item, quantity)
        Database.setInventory(inventory.itemsInside)
    }

    fun hasEnoughMaterials(material: MutableMap.MutableEntry<Materials, Int>): Boolean {
        val totalMaterialsOfThisType = 0
        val filteredInventoryItemsMap = inventory.itemsInside.asMap()
            .filterKeys { it.name == material.key.name }
        for(entry in filteredInventoryItemsMap.entries){
            println(entry.key)
        }
        return true
    }

}