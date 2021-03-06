package com.example.buriedtownclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buriedtownclone.homeequipment.Equipment
import com.example.buriedtownclone.homeequipment.Storage

class EquipmentActivity : AppCompatActivity(), Storage.ActionButtonListener {

    private lateinit var equipment: Equipment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)
    }

    override fun onResume() {
        super.onResume()
        updateClassesContext()
        showStatsBarFragment()
        setupEquipmentFragment()
    }

    private fun updateClassesContext(){
        VisualsUpdater.activity = this
        GameHandler.context = this
        TimeHandler.context = this
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(),"stats bar")
            .commit()
    }

    private fun setupEquipmentFragment(){
        equipment = getEquipmentObjectToShow()
        showEquipmentFragment()
    }

    private fun getEquipmentObjectToShow(): Equipment{
        return intent.getSerializableExtra("equipment") as Equipment
    }

    private fun showEquipmentFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.equipmentContainer, EquipmentFragment(equipment, this),"equipment")
            .commit()
    }

    override fun actionButtonFeedback(equipment: Equipment) {
        startActivity(equipment.getActivityToOpen(this))
    }
}