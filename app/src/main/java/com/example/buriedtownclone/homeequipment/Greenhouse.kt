package com.example.buriedtownclone.homeequipment

import com.example.buriedtownclone.*

object Greenhouse: Equipment("Greenhouse", 1, Definitions.greenhouseDescription) {

    override fun getActionButtonImage(): Int{
        return R.drawable.harvest
    }

    override fun initializeMaterialsList(materialsList: MutableList<MutableMap<Materials,Int>>?){
        materialsList?.add(0, mutableMapOf())
        materialsList?.add(1, mutableMapOf(Wood() to 5, Nail() to 8))
        materialsList?.add(2, mutableMapOf(Wood() to 15, Nail() to 20))
        materialsList?.add(3, mutableMapOf(Wood() to 30, Nail() to 40))
        materialsList?.add(4, mutableMapOf(Wood() to 75, Nail() to 80))
    }

    /*override fun upgrade(): Boolean {
            return true
    }*/

    private fun materialsEnough(): Boolean{
        return true
    }

    override fun performAction(){
        TODO("Not yet implemented")
    }

    fun reset(){
        level = 1
        HomeSpot.updateEquipmentList(this)
    }
}