package com.example.buriedtownclone

import java.io.Serializable

class Inventory : ItemsContainer(), Serializable {

    companion object{
        var slots = 15
    }

    init {
        super.slots = Companion.slots
    }

    override fun getClassName(): String {
        return "Inventory"
    }



}