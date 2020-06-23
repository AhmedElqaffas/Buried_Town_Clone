package com.example.buriedtownclone

import java.io.Serializable

class Inventory: ItemsContainer, Serializable {

    companion object{
        var slots = 15
    }

    constructor(){
        super.slots = Companion.slots
    }



}