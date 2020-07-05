package com.example.buriedtownclone.homeequipment

import androidx.fragment.app.Fragment
import com.example.buriedtownclone.*

object Greenhouse: Equipment("Greenhouse", 1, Definitions.greenhouseDescription) {

    override fun getFragment(): Fragment {
        return GreenhouseFragment()
    }

    fun getActionButtonImage(): Int{
        return R.drawable.harvest
    }

    override fun upgrade(): Boolean {
        if(materialsEnough()){
            level++
            Database.updateHomeEquipment()
            return true
        }
        return false
    }

    private fun materialsEnough(): Boolean{
        return true
    }

    override fun performAction(): Boolean {
        TODO("Not yet implemented")
    }

    fun reset(){
        level = 1
    }
}