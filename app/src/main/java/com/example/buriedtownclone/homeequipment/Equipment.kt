package com.example.buriedtownclone.homeequipment

import androidx.fragment.app.Fragment
import java.io.Serializable

abstract class Equipment(val name: String, var level: Int, val description: String): Serializable {
    fun upgradeLevel(){
        this.level += 1
    }

    abstract fun getFragment(): Fragment
}