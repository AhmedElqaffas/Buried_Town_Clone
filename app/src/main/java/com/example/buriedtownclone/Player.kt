package com.example.buriedtownclone

import android.content.Context

class Player(val context: Context) {
    private var hp: Int = 0
    private var hunger:Int = 0
    private var thirst: Int = 0
    private var locationX = 0
    private var locationY = 0
    private var inventory: Inventory = Inventory()

    var database: Database = Database(context)

    var gameHandler = GameHandler()

    fun getPlayerStats(): Array<Int>{
        return arrayOf(hp,hunger,thirst)
    }

    fun updateStatsFromDatabase(){
        this.setHealthPoints(database.getHealthPoints()!!)
        this.setHunger(database.getHunger())
        this.setThirst(database.getThirst())
    }

    fun setHunger(value: Int){
        if(value == 0) {
            gameHandler.endGame()
        }
        else{
            hunger = value
        }
    }
    fun setThirst(value: Int){
        if(value == 0) {
            gameHandler.endGame()
        }
        else{
            thirst = value
        }
    }
    fun setHealthPoints(value: Int){
        if(value == 0) {
            gameHandler.endGame()
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
        database.updatePlayerLocation(locationX, locationY)
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

}