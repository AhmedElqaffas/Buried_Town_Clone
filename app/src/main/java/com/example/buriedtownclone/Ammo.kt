package com.example.buriedtownclone

import java.io.Serializable

abstract class Ammo: Item, Serializable {
    constructor(name: String, imageResource: Int): super(name,imageResource){}

    override fun printTest(){
        println("Ammo")
    }
}