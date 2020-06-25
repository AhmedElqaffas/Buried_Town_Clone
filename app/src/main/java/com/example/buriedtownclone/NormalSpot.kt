package com.example.buriedtownclone

class NormalSpot: Spot() {

    override fun saveInDatabase() {
        val database = Database()
        database.saveSpot(this)
    }

    override fun getActivityToOpen(): Class<Any> {
        return SpotActivity::class.java as Class<Any>
    }

    override fun getActivityRequestCode(): Int {
        return Definitions.normalSpotRequestCode
    }
}