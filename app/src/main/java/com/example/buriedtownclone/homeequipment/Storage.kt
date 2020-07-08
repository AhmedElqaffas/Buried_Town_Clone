package com.example.buriedtownclone.homeequipment

import android.content.Context
import android.content.Intent
import com.example.buriedtownclone.*

object Storage: Equipment("Storage",1,"A box to store your items")/*, ItemsContainer*/{

    /*override var itemsInside: LinkedListMultimap<Item, String> =  LinkedListMultimap.create()
    override var slots: Int = 5*/


    override fun initializeMaterialsList(materialsList: MutableList<MutableMap<Materials, Int>>?) {
        materialsList?.add(0, mutableMapOf())
        materialsList?.add(1, mutableMapOf(Wood() to 5, Nail() to 8))
        materialsList?.add(2, mutableMapOf(Wood() to 15, Nail() to 20))
    }

    override fun getActionButtonImage(): Int {
       return R.drawable.locker_room
    }

    /*override fun getClassName(): String {
        return "Storage"
    }*/

    override fun getActivityToOpen(context: Context): Intent {
        val intent = Intent(context, SpotActivity::class.java)
        intent.putExtra(Definitions.spotItems,HomeSpot)
        return intent
    }

    override fun upgrade() {
        super.upgrade()
        HomeSpot.updateSlots(HomeSpot.slots + 5)
        saveUpgrade()
    }

    override fun performAction(actionButtonListener: ActionButtonListener) {
        actionButtonListener.actionButtonFeedback(this)
    }

    fun reset(){
        level = 1
        HomeSpot.updateEquipmentList(this)
    }

    interface ActionButtonListener{
        fun actionButtonFeedback(equipment: Equipment)
    }
}