package com.example.buriedtownclone

import android.database.Cursor
import java.io.Serializable

/* Implements serializable to be able to send an object of this class to the inventory activity
   using the put extra, as put extra doesn't allow sending normal objects
 */
abstract class Spot: ItemsContainer(), Serializable{
    var spotType: String = ""
    var cityX: Int = 0
    var cityY: Int = 0
    var locationWithinCity: Int = 0
    var visited: Boolean = false

    init {
        super.slots = Definitions.numberOfSlotsInSpot
    }

    fun visited(){
        if (!this.visited) {
            val database = Database()
            visited = true
            database.updateSpotVisit(this)
        }
    }

    override fun getClassName(): String {
        return "Building"
    }

    open fun saveInDatabase(){}

    open fun formObject(queryRow: Cursor){
        val database = Database()
        cityX = queryRow.getInt(queryRow.getColumnIndex("city_x"))
        cityY = queryRow.getInt(queryRow.getColumnIndex("city_y"))
        locationWithinCity = queryRow.getInt(queryRow.getColumnIndex("index_within_city"))
        visited = queryRow.getInt(queryRow.getColumnIndex("visited")) == 1 // to convert int to boolean
        itemsInside = database.unserializeItemsMap(queryRow)
        spotType = queryRow.getString(queryRow.getColumnIndex("type"))
    }

    abstract fun getActivityToOpen(): Class<Any>
    abstract fun getActivityRequestCode(): Int
}