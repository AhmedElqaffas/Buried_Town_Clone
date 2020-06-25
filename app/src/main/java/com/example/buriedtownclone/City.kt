package com.example.buriedtownclone

class City(val locationX: Int,val locationY: Int) {
    var numberOfSpotsWithin: Int = 0
    var spots: MutableList<Spot> = mutableListOf()

    init{
        chooseCitySpotsSize()
    }

    private fun chooseCitySpotsSize(){
        if(locationY == Definitions.homeY && locationX == Definitions.homeX){
            numberOfSpotsWithin = 6
        }
        else{
            numberOfSpotsWithin = (3..5).random();
        }

    }

    fun refreshSpotItems(spotToUpdate:Spot){
        spots[spotToUpdate.locationWithinCity] = spotToUpdate
    }

}