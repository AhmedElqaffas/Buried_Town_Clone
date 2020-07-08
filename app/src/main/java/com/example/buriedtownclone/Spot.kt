package com.example.buriedtownclone

import android.database.Cursor
import com.google.common.collect.LinkedListMultimap
import java.io.Serializable

/* Implements serializable to be able to send an object of this class to the inventory activity
   using the put extra, as put extra doesn't allow sending normal objects
 */
open class Spot(override var slots: Int) : ItemsContainer, Serializable{
    override var itemsInside: LinkedListMultimap<Item, String> = LinkedListMultimap.create()
    var spotType: String = ""
    var cityX: Int = 0
    var cityY: Int = 0
    var locationWithinCity: Int = 0
    var visited: Boolean = false


    /*init {
        super.setSlotsNumber(slots)
    }*/

    fun visited(){
        if (!this.visited) {
            visited = true
            Database.updateSpotVisit(this)
        }
    }

    override fun getClassName(): String {
        return "Building"
    }

    open fun saveInDatabase(){}

    open fun formObject(queryRow: Cursor){
        cityX = queryRow.getInt(queryRow.getColumnIndex("city_x"))
        cityY = queryRow.getInt(queryRow.getColumnIndex("city_y"))
        locationWithinCity = queryRow.getInt(queryRow.getColumnIndex("index_within_city"))
        visited = queryRow.getInt(queryRow.getColumnIndex("visited")) == 1 // to convert int to boolean
        itemsInside = Database.unserializeItemsMap(queryRow)
        spotType = queryRow.getString(queryRow.getColumnIndex("type"))
    }

    open fun getActivityToOpen(): Class<Any>{
        TODO("Not yet implemented")
    }
    open fun getActivityRequestCode(): Int{
        TODO("Not yet implemented")
    }
}