package com.example.buriedtownclone

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import java.io.Serializable

abstract class ItemsContainer: Serializable {
    var itemsInside: LinkedListMultimap<Item, String> = LinkedListMultimap.create()
    var slots = 0

    open fun decrementItemQuantity(item: Item){
        changeSlotQuantity(item, 0)
    }

    open fun addItem(item: Item): Boolean{

        if(uniqueItem(item) && existsSpaceForItem()){
            itemsInside.put(item, "1")
            return true
        }
        else if(!uniqueItem(item)){
            return incrementExistingItem(item)
        }
        return false
    }

    /**
     * Description: Takes an item and either decrements or increments its value according to the other
     * parameter, if the other parameter is 0: it decrements, otherwise: it increments
     */
    private fun changeSlotQuantity(item: Item?, incrementOrDecrement: Int){
        val initialQuantity: Int = itemsInside[item].last().toInt()

        val changedQuantity: Int = if (incrementOrDecrement == 0){ initialQuantity - 1}
                                   else {initialQuantity + 1}

        val initialItemValuesList = itemsInside.get(item)
        val keysOrderBeforeChange: MutableSet<Item> = mutableSetOf()
        keysOrderBeforeChange.addAll(itemsInside.keys())
        initialItemValuesList.removeAt(initialItemValuesList.lastIndex)

        if(changedQuantity != 0){
            initialItemValuesList.add(changedQuantity.toString())
        }

        val orderPreserverMap: LinkedListMultimap<Item, String> = LinkedListMultimap.create()

        for(key in keysOrderBeforeChange){
            if(key == item){
                orderPreserverMap.putAll(key,initialItemValuesList)
            }

            else{
                orderPreserverMap.putAll(key, itemsInside.get(key))
            }
        }

        itemsInside = orderPreserverMap
    }

    private fun uniqueItem(item: Item): Boolean{
        for(entry in itemsInside.keys()){
            if(entry::class.qualifiedName == item::class.qualifiedName){
                return false
            }
        }
        return true
    }
    private fun existsSpaceForItem(): Boolean{
        return itemsInside.size() < slots
    }

    private fun incrementExistingItem(item: Item): Boolean{

        val existingItem = getItemFromClassName(item)

        val initialQuantity: Int = itemsInside[existingItem].last().toInt()
        if(initialQuantity == 15 && existsSpaceForItem()){
            itemsInside.put(existingItem,"1")
            return true
        }
        else if(initialQuantity < 15) {
            changeSlotQuantity(existingItem,1)
            return true
        }
        return false

    }

    /*
     * In the list objects are saved as com.example.buriedtownclone.Water@e37032c for example
     * the @e37032c represents a problem as it differs from time to time so we need to get the item "water"
     * regardless of the letters after @
     * Will do so by getting all water items in the list then returning the last item of those items
     */
    private fun getItemFromClassName(item: Item): Item? {
        var lastMatchingItem: Item? = null
        for (entry in itemsInside.keys()) {
            if (entry::class.qualifiedName == item::class.qualifiedName) {
                lastMatchingItem = entry
            }
        }
        return lastMatchingItem
    }

    fun getQuantityOfItemAt(index: Int): String{
        return itemsInside.entries()[index].value
    }

    fun getItemAt(index: Int): Item{
        return itemsInside.entries()[index].key
    }

    abstract fun getClassName(): String
}