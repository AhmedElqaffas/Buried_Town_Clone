package com.example.buriedtownclone

import com.example.buriedtownclone.homeequipment.Bed
import com.example.buriedtownclone.homeequipment.Equipment
import com.example.buriedtownclone.homeequipment.Greenhouse

class HomeSpot: Spot() {

    companion object{
        val equipmentList = mutableListOf<Equipment>(Greenhouse(), Bed())
    }

    override fun saveInDatabase() {
        val database = Database()
        database.saveHomeSpot(this)
    }

    fun updateEquipmentInDatabase(){
        val database = Database()
        database.updateHomeEquipment()
    }

   override fun getActivityToOpen(): Class<Any>{
        return HomeSpotActivity::class.java as Class<Any>
    }

    override fun getActivityRequestCode(): Int {
        return Definitions.homeSpotRequestCode
    }

    fun getEquipment(name: String): Equipment{
        return equipmentList.single { it.name == name }
    }
}