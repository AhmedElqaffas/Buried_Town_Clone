package com.example.buriedtownclone

import java.io.Serializable

abstract class Materials(name: String, imageResource: Int): Item(name, imageResource), Serializable {

    override fun isConsumable():Boolean {
        return false
    }
}