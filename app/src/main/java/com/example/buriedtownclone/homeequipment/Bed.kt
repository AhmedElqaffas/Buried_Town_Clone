package com.example.buriedtownclone.homeequipment

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

    override fun performAction(){
        updatePlayerStats()
    }

    private fun updatePlayerStats(){
        Player.updateHP(100)
        Player.updateThirst(-10)
        Player.updateHunger(-7)
    }
}