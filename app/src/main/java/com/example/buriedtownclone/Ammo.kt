package com.example.buriedtownclone

import java.io.Serializable

abstract class Ammo(name: String, imageResource: Int) : Item(name, imageResource), Serializable {

    override fun isConsumable():Boolean {
        return false
    }
}