package com.example.buriedtownclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buriedtownclone.homeequipment.Equipment

class EquipmentActivity : AppCompatActivity() {

    private var player = Player()
    private lateinit var equipment: Equipment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

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
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(player),"stats bar")
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
        supportFragmentManager.beginTransaction().replace(R.id.equipmentContainer, equipment.getFragment(),"equipment")
            .commit()
    }
}