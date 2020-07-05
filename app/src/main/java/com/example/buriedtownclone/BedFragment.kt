package com.example.buriedtownclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.buriedtownclone.homeequipment.Bed
import kotlinx.android.synthetic.main.fragment_bed.*
import kotlinx.android.synthetic.main.fragment_bed.titleTextView

class BedFragment : Fragment() {

    private lateinit var inflated: ConstraintLayout
    private lateinit var bed: Bed
    private val homeSpot = HomeSpot

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflated = inflater.inflate(R.layout.fragment_bed, container, false) as ConstraintLayout
        return inflated
    }

    override fun onResume() {
        super.onResume()
        setupFragment()
    }

    override fun onPause() {
        super.onPause()
        homeSpot.updateEquipment(bed)

    }

    private fun setupFragment(){
        initializeBedObject()
        setTitleLevel()
        setDescription()
        setSleepButtonClickListener()
    }

    private fun initializeBedObject(){
        bed = homeSpot.getEquipment("Bed") as Bed
    }

    private fun setTitleLevel(){
        titleTextView.text = "${titleTextView.text} - Level ${bed.level}"
    }

    private fun setDescription(){
        equipmentDescription.text = bed.description
    }

    private fun setSleepButtonClickListener(){
        sleepButton.setOnClickListener { sleepButtonClicked() }
    }

    private fun sleepButtonClicked(){
        updatePlayerStats()
    }

    private fun updatePlayerStats(){
        Player.updateHP(100)
        Player.updateThirst(-10)
        Player.updateHunger(-7)
    }

}