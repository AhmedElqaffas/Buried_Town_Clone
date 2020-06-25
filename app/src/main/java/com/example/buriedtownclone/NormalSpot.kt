package com.example.buriedtownclone

import android.database.Cursor

class NormalSpot: Spot() {

    override fun saveInDatabase() {
        var database = Database()
        database.saveSpot(this)
    }

    override fun getActivityToOpen(): Class<Any> {
        return SpotActivity::class.java as Class<Any>
    }

    override fun getActivityRequestCode(): Int {
        return Definitions.normalSpotRequestCode
    }
}