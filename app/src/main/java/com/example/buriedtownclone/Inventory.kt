package com.example.buriedtownclone

import java.io.Serializable

object Inventory : ItemsContainer(), Serializable {

    init {
        super.slots = 15
    }

    override fun getClassName(): String {
        return "Inventory"
    }

    fun hasEnoughMaterials(material: MutableMap.MutableEntry<Materials, Int>): Boolean {
        var totalMaterialsOfThisType = 0
        for(slot in itemsInside.entries()){
            if(slot.key.name == material.key.name){
                totalMaterialsOfThisType += slot.value.toInt()
                if(totalMaterialsOfThisType >= material.value){
                    return true
                }
            }
        }
        return false
    }



}