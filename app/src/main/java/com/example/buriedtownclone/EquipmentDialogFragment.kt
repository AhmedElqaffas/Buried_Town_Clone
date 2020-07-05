package com.example.buriedtownclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.buriedtownclone.homeequipment.Equipment
import kotlinx.android.synthetic.main.alert_dialog.*
import kotlinx.android.synthetic.main.fragment_greenhouse.*


class EquipmentDialogFragment(private val equipmentObject: Equipment): androidx.fragment.app.DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.alert_dialog, container, false)
    }

    override fun onResume() {
        super.onResume()
        setMessageText()
        setClickListeners()
    }

    private fun setMessageText(){
        upgradeMessage.append(" ${equipmentObject.name}?")
    }

    private fun setClickListeners(){
        confirmUpgrade.setOnClickListener { confirmUpgradeButtonClicked() }
        cancelUpgrade.setOnClickListener { dialog?.dismiss() }
    }

    private fun confirmUpgradeButtonClicked(){
        println(equipmentObject.upgrade())
        updateFragmentLevelText()
    }

    private fun updateFragmentLevelText(){
        val fragment = activity?.supportFragmentManager?.findFragmentByTag("equipment")
        fragment?.titleTextView?.text = "${equipmentObject.name} - Level ${equipmentObject.level}"
        this.dismiss()
    }

}