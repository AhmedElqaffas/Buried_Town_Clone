package com.example.buriedtownclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.io.Serializable

/* Implements serializable to be able to send an object of this class to the inventory activity
   using the put extra, as put extra doesn't allow sending normal objects
 */
class Spot: ItemsContainer, Serializable{
    var spotType: String = ""
    var cityX: Int = 0
    var cityY: Int = 0
    var locationWithinCity: Int = 0
    var visited: Boolean = false

    constructor(){
        super.slots = Definitions.numberOfSlotsInSpot
    }

    fun visited(){
        var database = Database()
        visited = true
        database.updateSpotVisit(this)
    }

}