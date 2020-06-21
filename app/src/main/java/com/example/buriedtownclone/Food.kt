package com.example.buriedtownclone

import java.io.Serializable

abstract class Food: Item, Serializable {
    var hungerBonus: Int = 0

    constructor(name: String, hungerBonus: Int, imageResource: Int): super(name,imageResource){
        this.hungerBonus = hungerBonus
    }

    override fun printTest(){
        println("FOOD")
    }

}