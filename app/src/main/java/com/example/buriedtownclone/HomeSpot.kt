package com.example.buriedtownclone

import com.example.buriedtownclone.homeequipment.Bed
import com.example.buriedtownclone.homeequipment.Equipment
import com.example.buriedtownclone.homeequipment.Greenhouse

object HomeSpot: Spot() {

    val equipmentList: MutableList<Equipment> = mutableListOf(Greenhouse, Bed)

    override fun saveInDatabase() {
        Database.saveHomeSpot(this)
    }

    fun updateEquipment(equipment: Equipment){
        updateEquipmentList(equipment)
        updateEquipmentInDatabase()
    }

    private fun updateEquipmentList(equipment: Equipment){
        equipmentList.removeAll { it.name == equipment.name }
        equipmentList.add(equipment)
    }

    private fun updateEquipmentInDatabase(){
        Database.updateHomeEquipment()
    }

    fun updateEquipmentFromDatabase(){
        Database.getHomeEquipment()
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