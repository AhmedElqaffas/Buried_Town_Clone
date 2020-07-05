package com.example.buriedtownclone

import android.app.FragmentManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.example.buriedtownclone.homeequipment.Equipment
import com.example.buriedtownclone.homeequipment.Greenhouse
import kotlinx.android.synthetic.main.fragment_greenhouse.*


class GreenhouseFragment : Fragment() {

    private lateinit var inflated: ConstraintLayout
    private lateinit var greenhouse: Greenhouse
    private val homeSpot = HomeSpot

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflated = inflater.inflate(R.layout.fragment_greenhouse, container, false) as ConstraintLayout
        return inflated
    }

    override fun onPause() {
        super.onPause()
        homeSpot.updateEquipment(greenhouse)
    }

    override fun onResume() {
        super.onResume()
        setupFragment()
    }

    private fun setupFragment(){
        initializeGreenHouseObject()
        setTitleLevel()
        setDescription()
        setActionButtonImage()
        setClickListeners()
    }

    private fun initializeGreenHouseObject(){
        greenhouse = homeSpot.getEquipment("Greenhouse") as Greenhouse
    }

    private fun setTitleLevel(){
        titleTextView.text = "${titleTextView.text} - Level ${greenhouse.level}"
    }

    private fun setDescription(){
        equipmentDescription.text = greenhouse.description
    }

    private fun setActionButtonImage(){
        actionButton.setImageResource(greenhouse.getActionButtonImage())
    }

    private fun setClickListeners(){
        upgradeButton.setOnClickListener { upgradeButtonClicked() }
        actionButton.setOnClickListener { actionButtonClicked() }
    }

    private fun upgradeButtonClicked(){
        VisualsUpdater.showEquipmentDialog(greenhouse, fragmentManager)
    }

    private fun actionButtonClicked(){
        greenhouse.performAction()
    }

}