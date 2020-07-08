package com.example.buriedtownclone

import com.google.common.collect.LinkedListMultimap
import java.io.Serializable

object Inventory : ItemsContainer, Serializable {

    override var itemsInside: LinkedListMultimap<Item, String> = LinkedListMultimap.create()
    override var slots: Int = 15

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