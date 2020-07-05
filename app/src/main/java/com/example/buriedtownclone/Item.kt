package com.example.buriedtownclone

import java.io.Serializable

abstract class Item(var name: String, var imageResource: Int): Serializable {

    fun getInstanceType(): Any{
        return Class.forName(this::class.qualifiedName!!).newInstance()
    }

    open fun activateItemEffect(){}
    abstract fun isConsumable(): Boolean
}