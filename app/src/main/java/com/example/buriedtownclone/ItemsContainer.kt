package com.example.buriedtownclone

import com.google.common.collect.LinkedListMultimap
import java.io.Serializable

abstract class ItemsContainer: Serializable {
    var itemsInside: LinkedListMultimap<Item, String> = LinkedListMultimap.create()
    var slots = 0

    open fun decrementItemQuantity(itemIndex: Int){
        decrementOrRemoveItem(itemIndex)
    }

    private fun decrementOrRemoveItem(itemIndex: Int){
        val mapEntryToDecrement = itemsInside.entries()[itemIndex]
        val initialValue = mapEntryToDecrement.value.toInt()
        if(initialValue == 1){
            itemsInside.entries().removeAt(itemIndex)
        }
        else{
            mapEntryToDecrement.setValue((initialValue - 1).toString())
        }
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
        val firstNonFullItemSlot: MutableMap.MutableEntry<Item, String>? = getItemFromClassName(item)
        if(firstNonFullItemSlot == null && existsSpaceForItem()){
            itemsInside.put(item,"1")
            return true
        }
        else if(firstNonFullItemSlot != null){
            firstNonFullItemSlot.setValue((firstNonFullItemSlot.value.toInt() + 1).toString())
            return true
        }
        return false
    }

    /*
     * In the list objects are saved as com.example.buriedtownclone.Water@e37032c for example
     * the @e37032c represents a problem as it differs from time to time so we need to get the item "water"
     * regardless of the letters after @
     */
    private fun getItemFromClassName(item: Item): MutableMap.MutableEntry<Item, String>?{
        for (entry in itemsInside.entries()) {
            if (itemsMatch(item, entry) && !isSlotFull(entry)) {
                return entry
            }
        }
        return null
    }

    private fun itemsMatch(itemClicked: Item, mapEntry: MutableMap.MutableEntry<Item, String>): Boolean{
        return mapEntry.key::class.qualifiedName == itemClicked::class.qualifiedName
    }

    private fun isSlotFull(mapEntry: MutableMap.MutableEntry<Item, String>): Boolean{
        return mapEntry.value.toInt() == 15
    }

    fun getQuantityOfItemAt(index: Int): String{
        return itemsInside.entries()[index].value
    }

    fun getItemAt(index: Int): Item{
        return itemsInside.entries()[index].key
    }

    abstract fun getClassName(): String
}