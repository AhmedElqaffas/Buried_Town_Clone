package com.example.buriedtownclone.homeequipment

import androidx.fragment.app.Fragment
import java.io.Serializable

abstract class Equipment(var name: String, var level: Int, var description: String): Serializable {
    fun upgradeLevel(){
        this.level += 1
    }

    abstract fun getFragment(): Fragment
    abstract fun upgrade(): Boolean
    abstract fun performAction(): Boolean
}