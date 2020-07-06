package com.example.buriedtownclone.homeequipment

import androidx.fragment.app.Fragment
import com.example.buriedtownclone.Materials
import java.io.Serializable
import java.lang.Exception

abstract class Equipment(var name: String, var level: Int, var description: String): Serializable {

    private val materialsList: MutableList<MutableMap<Materials, Int>>? = mutableListOf()

    init {
        initializeMaterialsList(materialsList)
    }

    fun upgradeLevel(){
        this.level += 1
    }

    fun isUpgradeable(): Boolean{
        return materialsList?.size!! > level
    }

    fun getMaterialsNeededToUpgrade(): MutableMap<Materials, Int>?{
        return materialsList?.get(level)
    }


    /**
     * Description: Each entry in the list represents the materials needed to upgrade to the next level.
     * For ex: index 0 represents materials needed to upgrade from level 0 to level 1
     */
    abstract fun initializeMaterialsList(materialsList:MutableList<MutableMap<Materials, Int>>?)
    abstract fun upgrade(): Boolean
    abstract fun performAction()
    abstract fun getActionButtonImage(): Int
}