package com.example.buriedtownclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.buriedtownclone.homeequipment.Equipment
import kotlinx.android.synthetic.main.fragment_equipment.*


class EquipmentFragment(private val equipment: Equipment) : Fragment() {

    private lateinit var inflated: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflated = inflater.inflate(R.layout.fragment_equipment, container, false) as ConstraintLayout
        return inflated
    }

    override fun onResume() {
        super.onResume()
        setupFragment()
    }

    private fun setupFragment(){
        setTitleLevel()
        setDescription()
        setActionButtonImage()
        setClickListeners()
    }

    private fun setTitleLevel(){
        titleTextView.text = "${equipment.name} - Level ${equipment.level}"
    }

    private fun setDescription(){
        equipmentDescription.text = equipment.description
    }

    private fun setActionButtonImage(){
        actionButton.setImageResource(equipment.getActionButtonImage())
    }

    private fun setClickListeners(){
        upgradeButton.setOnClickListener { upgradeButtonClicked() }
        actionButton.setOnClickListener { actionButtonClicked() }
    }

    private fun upgradeButtonClicked(){
        if(equipment.isUpgradeable()){
            VisualsUpdater.showEquipmentDialog(equipment, fragmentManager)
        }
        else{
            Toast.makeText(context, "This Equipment is fully upgraded", Toast.LENGTH_LONG).show()
        }

    }

    private fun actionButtonClicked(){
        equipment.performAction()
    }

}