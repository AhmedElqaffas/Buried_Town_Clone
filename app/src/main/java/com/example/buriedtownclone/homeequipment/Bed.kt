package com.example.buriedtownclone.homeequipment

import android.content.Context
import android.content.Intent
import com.example.buriedtownclone.*

object Bed: Equipment("Bed", 1, Definitions.bedDescription) {

    /**
     * Description: Bed Can't be Upgraded
     */
    override fun initializeMaterialsList(materialsList: MutableList<MutableMap<Materials,Int>>?){
        materialsList?.add(0, mutableMapOf())
    }

    override fun getActionButtonImage(): Int {
        return R.drawable.sleep
    }

    override fun performAction(actionButtonListener: Storage.ActionButtonListener){
        updatePlayerStats()
    }

    override fun getActivityToOpen(context: Context): Intent {
        TODO ("Implement Another Activity if needed")
    }

    private fun updatePlayerStats(){
        Player.updateHP(100)
        Player.updateThirst(-10)
        Player.updateHunger(-7)
    }

}