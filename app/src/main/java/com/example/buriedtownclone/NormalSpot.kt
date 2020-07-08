package com.example.buriedtownclone

class NormalSpot: Spot(Definitions.numberOfSlotsInSpot) {

    override fun saveInDatabase() {
        Database.saveSpot(this)
    }

    override fun getActivityToOpen(): Class<Any> {
        return SpotActivity::class.java as Class<Any>
    }

    override fun getActivityRequestCode(): Int {
        return Definitions.normalSpotRequestCode
    }
}