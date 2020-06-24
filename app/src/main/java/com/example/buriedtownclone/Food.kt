package com.example.buriedtownclone

import java.io.Serializable

abstract class Food: Item, Serializable {
    var hungerBonus: Int = 0
    var thirstBonus: Int = 0

    constructor(name: String, hungerBonus: Int, thirstBonus: Int, imageResource: Int): super(name,imageResource){
        this.hungerBonus = hungerBonus
        this.thirstBonus = thirstBonus
    }

    override fun activateItemEffect(player: Player) {
        player.updateHunger(hungerBonus)
        player.updateThirst(thirstBonus)
    }

    override fun isConsumable():Boolean {
        return true
    }
}