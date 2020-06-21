package com.example.buriedtownclone

import java.io.Serializable

abstract class Item: Serializable {
    var name: String = ""
    var imageResource: Int = 0

    constructor(name: String, imageResource: Int){
        this.name = name
        this.imageResource = imageResource
    }

    fun getInstanceType(): Any{
        return Class.forName(this::class.qualifiedName!!).newInstance()
    }

    open fun printTest(){
        println("The Type is ....::")
    }
}