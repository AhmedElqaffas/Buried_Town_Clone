package com.example.buriedtownclone

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.resources.TextAppearance
import org.w3c.dom.Text
import java.lang.Integer.parseInt


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ItemsFragment(val itemContainerType: ItemsContainer) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var parentActivityContext: Context
    lateinit var inflated: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivityContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setupFragment(inflater, container)
        return inflated
    }

    private fun setupFragment(inflater: LayoutInflater, container: ViewGroup?): View{

         inflated = inflater.inflate(R.layout.fragment_items, container, false)
                as GridLayout
        drawSlots()
        setupSlotViews()

        return inflated
    }

    private fun drawSlots(){
        for(i in 0 until itemContainerType.slots){
            var slot = drawCurrentSlot()
            customizeCurrentSlot(slot)
        }
    }
    private fun drawCurrentSlot(): TextView{
        var newSlot = TextView(context)
        inflated.addView(newSlot)
        return newSlot
    }
    private fun customizeCurrentSlot(slot: TextView){
        setSlotTag(slot)
        setSlotWeightInParent(slot)
        customizeTextStyle(slot)
    }
    private fun setSlotTag(slot: View){
        var indexWithinParent: Int = (slot.parent as GridLayout).indexOfChild(slot)
        slot.tag = "slot$indexWithinParent"
    }
    private fun setSlotWeightInParent(slot: TextView){
        val param: GridLayout.LayoutParams = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, 1f)
        )

        slot.layoutParams = param
    }
    private fun customizeTextStyle(slot: TextView){
        slot.gravity = Gravity.BOTTOM or Gravity.END // bitwise or
        slot.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        slot.setTextColor(resources.getColor(R.color.quantity))
        slot.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        slot.setTypeface(slot.typeface, Typeface.BOLD)
    }

    private fun setupSlotViews(){
        println("SetupSlotView Called")
        var slotsFound = 0
        for(slot in inflated.children){
            if(existsItemToPutInSlot(slotsFound) ){
                addListener(slot as TextView, slotsFound)
                addItemImageAndQuantity(slot, slotsFound)
                slotsFound++
            }
        }
    }
    private fun existsItemToPutInSlot(slotIndex: Int): Boolean{
        return slotIndex < itemContainerType.itemsInside.size
    }

    private fun addListener(slot: TextView, slotsFound: Int){
            slot.setOnClickListener { slotClickListener(slot, slotsFound) }
    }

    private fun slotClickListener(view: View, slotIndex: Int){
        addItemToInventory(view, slotIndex)
    }
    private fun addItemToInventory(view: View, slotIndex: Int){
        reduceItemQuantityInSpot(slotIndex)
    }
    private fun reduceItemQuantityInSpot(slotIndex: Int){
        var newQuantity = getItemQuantity(slotIndex) - 1
        setItemQuantity(slotIndex,newQuantity)
        updateItemQuantityTextView(slotIndex, newQuantity)
        refreshLayoutIfItemIsFinished(newQuantity)
    }
    private fun setItemQuantity(slotIndex: Int, newQuantity: Int){
        itemContainerType.setItemQuantity(getItem(slotIndex), newQuantity)
    }
    private fun refreshLayoutIfItemIsFinished(newQuantity: Int){
        println("$newQuantity in refreshLayoutIfItemFinished")
        if(newQuantity == 0){
            println("refreshCalled")
            refreshLayout()
        }
    }
    private fun refreshLayout(){
        resetSlots()
        setupSlotViews()
    }
    private fun resetSlots(){
        for(slot in inflated.children){
            (slot as TextView).text = ""
            slot.setBackgroundResource(0)
            slot.setOnClickListener{}
            slot.removeCallbacks {  }
        }
    }

    private fun addItemImageAndQuantity(slot: View, slotIndex: Int){
        addItemImage(slot, slotIndex)
        addItemQuantity(slot,slotIndex)

    }
    private fun addItemImage(slot: View, slotIndex: Int){
        slot.setBackgroundResource(getItem(slotIndex).imageResource)
    }

    private fun getItem(slotIndex: Int): Item {
        return ArrayList<Item>(itemContainerType.itemsInside.keys)[slotIndex]
    }

    private fun addItemQuantity(slot: View, slotIndex: Int){
        var quantity = getItemQuantity(slotIndex)
        updateItemQuantityTextView(slotIndex,quantity)
    }
    private fun getItemQuantity(slotIndex: Int): Int{
        var stringQuantity = ArrayList<String>(itemContainerType.itemsInside.values)[slotIndex]
        return parseInt(stringQuantity)
    }

    private fun updateItemQuantityTextView(slotIndex: Int, quantity: Int){
        var slotView = inflated.getChildAt(slotIndex) as TextView
        slotView.text = quantity.toString()
    }
}