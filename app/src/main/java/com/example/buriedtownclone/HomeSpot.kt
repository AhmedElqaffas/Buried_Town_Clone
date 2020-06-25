package com.example.buriedtownclone

import android.database.Cursor

class HomeSpot: Spot() {
    var farmLevel: Int = 0

    override fun saveInDatabase() {
        var database = Database()
        database.saveHomeSpot(this)
    }

    override fun formObject(queryRow: Cursor) {
        super.formObject(queryRow)
        farmLevel = queryRow.getInt(queryRow.getColumnIndex("farm_level"))
    }

   override fun getActivityToOpen(): Class<Any>{
        return HomeSpotActivity::class.java as Class<Any>
    }

    override fun getActivityRequestCode(): Int {
        return Definitions.homeSpotRequestCode
    }
}