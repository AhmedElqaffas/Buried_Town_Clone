package com.example.buriedtownclone

import java.io.Serializable

abstract class Drink: Item, Serializable {
    var thirstBonus: Int = 0

    constructor(name: String, thirstBonus: Int, imageResource: Int): super(name,imageResource){
        this.thirstBonus = thirstBonus
    }

    override fun activateItemEffect() {
        Player.updateThirst(thirstBonus)
    }

    override fun isConsumable():Boolean {
        return true
    }
}