package com.example.buriedtownclone


import java.io.Serializable

abstract class ItemsContainer: Serializable {
    var itemsInside: LinkedHashMap<Item,String> = LinkedHashMap()
    var slots = 0

    fun setItemQuantity(item: Item, quantity: Int) {
        itemsInside[item] = quantity.toString()
        if (quantity == 0) {
            itemsInside.remove(item)
        }
    }

    open fun addItem(item: Item): Boolean{
        println("SIZE IS   ${itemsInside.size}")
        for (item in itemsInside)
            println(item.key::class.qualifiedName)
        if(uniqueItem(item) && existsSpaceForItem()){
            itemsInside[item] = "1"
            return true
        }
        else if(!uniqueItem(item)){
            incrementExistingItem(item)
            return true
        }
        return false
    }

    private fun uniqueItem(item: Item): Boolean{
        for(entry in itemsInside){
            if(entry.key::class.qualifiedName == item::class.qualifiedName){
                return false
            }
        }
        return true
    }
    private fun existsSpaceForItem(): Boolean{
        return itemsInside.size < slots
    }
    private fun incrementExistingItem(item: Item){
        for(entry in itemsInside){
            if(entry.key::class.qualifiedName == item::class.qualifiedName){
                entry.setValue((entry.value.toInt()+1).toString())
            }
        }
    }
}