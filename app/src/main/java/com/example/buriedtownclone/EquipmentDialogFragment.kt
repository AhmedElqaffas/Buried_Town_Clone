package com.example.buriedtownclone

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.buriedtownclone.homeequipment.Equipment
import kotlinx.android.synthetic.main.alert_dialog.*
import kotlinx.android.synthetic.main.fragment_equipment.*


class EquipmentDialogFragment(private val equipmentObject: Equipment): androidx.fragment.app.DialogFragment(){

    private var materialsEnoughToUpgrade = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.alert_dialog, container, false)
    }

    override fun onResume() {
        super.onResume()
        setMessageText()
        showRequiredResources()
        setClickListeners()
    }

    private fun setMessageText(){
        upgradeMessage.append(" ${equipmentObject.name}?")
    }

    private fun showRequiredResources(){
        for(material in equipmentObject.getMaterialsNeededToUpgrade()!!.entries){
            addXMLComponents(material)
        }
    }

    private fun addXMLComponents(material: MutableMap.MutableEntry<Materials, Int>) {
        val materialImageView = addMaterialImage(material)
        val materialQuantityTextView = addMaterialQuantityTextView(material)
        requiredMaterialsContainer.addView(materialImageView)
        requiredMaterialsContainer.addView(materialQuantityTextView)
        enableOrDisableUpgradeButton()
    }

    private fun addMaterialImage(material: MutableMap.MutableEntry<Materials, Int>): ImageView{
        val materialImageView =ImageView(context)
        materialImageView.setImageResource(material.key.imageResource)
        addLeftMargin(materialImageView, 15)
        return materialImageView
    }

    private fun addMaterialQuantityTextView(material: MutableMap.MutableEntry<Materials, Int>): TextView{
        val materialQuantity = TextView(context)
        materialQuantity.text = "x${material.value}"
        materialQuantity.textSize = (18).toFloat()
        addLeftMargin(materialQuantity, 8)
        setQuantityTextColor(material, materialQuantity)
        return materialQuantity
    }

    private fun setQuantityTextColor(material: MutableMap.MutableEntry<Materials, Int>, materialQuantity: TextView) {
        if(Inventory.hasEnoughMaterials(material)){
            materialQuantity.setTextColor(Color.GREEN)
            materialsEnoughToUpgrade = true
        }
        else{
            materialQuantity.setTextColor(Color.RED)
        }
    }

    private fun addLeftMargin(view: View, marginDP: Int){
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        params.marginStart = dpToPx((marginDP).toFloat())
        view.layoutParams = params
    }

    private fun enableOrDisableUpgradeButton(){
        if(!materialsEnoughToUpgrade)
            confirmUpgrade.isEnabled = false
    }

    private fun setClickListeners(){
        confirmUpgrade.setOnClickListener { confirmUpgradeButtonClicked() }
        cancelUpgrade.setOnClickListener { dialog?.dismiss() }
    }

    private fun confirmUpgradeButtonClicked(){
        equipmentObject.upgrade()
        updateFragmentLevelText()
        this.dismiss()
    }

    private fun updateFragmentLevelText(){
        val fragment = activity?.supportFragmentManager?.findFragmentByTag("equipment")
        fragment?.titleTextView?.text = "${equipmentObject.name} - Level ${equipmentObject.level}"
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
            context?.resources?.displayMetrics
        ).toInt()
    }



}