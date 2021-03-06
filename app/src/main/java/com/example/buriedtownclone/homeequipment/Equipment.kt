package com.example.buriedtownclone.homeequipment

import android.content.Context
import android.content.Intent
import com.example.buriedtownclone.*
import java.io.Serializable

// @Transient excludes field from Gson serialization

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

    open fun upgrade(){
        Player.consumeBuildingMaterials(materialsList?.get(level))
        upgradeLevel()
    }

    fun saveUpgrade(){
        HomeSpot.updateEquipment(this)
        Database.setInventory(Inventory.itemsInside)
    }


    /**
     * Description: Each entry in the list represents the materials needed to upgrade to the next level.
     * For ex: index 0 represents materials needed to upgrade from level 0 to level 1
     */
    abstract fun initializeMaterialsList(materialsList:MutableList<MutableMap<Materials, Int>>?)
    abstract fun performAction(actionButtonListener: Storage.ActionButtonListener)
    abstract fun getActionButtonImage(): Int
    abstract fun getActivityToOpen(context: Context): Intent
}