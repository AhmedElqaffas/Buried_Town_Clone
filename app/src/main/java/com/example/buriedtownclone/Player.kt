package com.example.buriedtownclone

class Player() {

    companion object{

        private var hp: Int = 100
        private var hunger:Int = 100
        private var thirst: Int = 100
        private var locationX = 0
        private var locationY = 0
        private var inventory: Inventory = Inventory()

        private var database: Database = Database()
        private var gameHandler = GameHandler()
        private var visualsUpdater = VisualsUpdater()
    }


    fun updateStatsFromDatabase(){

        this.setHealthPoints(database.getHealthPoints()!!)
        hunger = database.getHunger()
        thirst = database.getThirst()
        inventory.itemsInside = database.getInventory()
        var location = database.getPlayerLocation()
        locationX = location[0]
        locationY = location[1]
    }

   private fun setHealthPoints(value: Int){
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

    fun updateHP(hpBonus: Int){
        hp += hpBonus
        if(hp > 100)
            hp = 100
        else if(hp <= 0)
            gameHandler.endGame()
        visualsUpdater.showStatsInStatsBar(this)
        database.setHP(hp)
    }

    fun updateHunger(hungerBonus: Int){
        hunger += hungerBonus
        if(hunger > 100)
            hunger = 100
        else if(hunger <= 0) {
            gameHandler.endGame()
        }
        visualsUpdater.showStatsInStatsBar(this)
        database.setHunger(hunger)
    }
    fun updateThirst(thirstBonus: Int){
        thirst += thirstBonus
        if(thirst > 100)
            thirst = 100
        if(thirst <= 0) {
            gameHandler.endGame()
        }
        visualsUpdater.showStatsInStatsBar(this)
        database.setThirst(thirst)
    }

    fun addToInventory(item: Item, quantity: String){
        inventory.itemsInside.put(item, quantity)
        database.setInventory(inventory.itemsInside)
    }

}