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
}