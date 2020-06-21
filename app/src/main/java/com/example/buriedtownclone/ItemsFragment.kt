package com.example.buriedtownclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import java.lang.Integer.parseInt


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ItemsFragment(val type: Any) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val stringToSpotImage: HashMap<String, Int> = hashMapOf(Definitions.apples to R.drawable.apple,
        Definitions.water to R.drawable.water, Definitions.tuna to R.drawable.tuna,
        Definitions.mm9 to R.drawable.mm9)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var inflated = setupFragment(inflater, container)
        return inflated
    }

    private fun setupFragment(inflater: LayoutInflater, container: ViewGroup?): View{
        var inflated: GridLayout
        if(type is Spot){
            inflated = inflater.inflate(R.layout.fragment_items, container, false) as GridLayout
            setupSlotViews(inflated)
        }
        // else : type is Inventory
        else{
            // TODO: Implement inventory style
            throw Exception("not yet")
        }

        return inflated
    }

    private fun setupSlotViews(inflated: GridLayout){
        var slotsFound = 0
        for(slot in inflated.children){
            if(slot is TextView && existsItemToPutInSlot(slotsFound) ){
                addListener(slot, slotsFound)
                addItemImageAndQuantity(slot, slotsFound)
                slotsFound++
            }
        }
    }

    private fun addListener(slot: TextView, slotsFound: Int){
            slot.setOnClickListener { slotClickListener(slot, slotsFound) }
    }
    private fun existsItemToPutInSlot(slotIndex: Int): Boolean{
        return slotIndex < (type as Spot).itemsInside.size
    }

    /*private fun getItemToPutInThisSlot(slotIndex: Int): MutableList<String>{
        var itemNameAndQuantity: MutableMap<Item,Int> = mutableListOf()
        var key = getItemKey(slotIndex)
        var value = getItemValue(slotIndex)
        itemNameAndQuantity.add(key)
        itemNameAndQuantity.add(value)
        return itemNameAndQuantity
    }*/


    private fun slotClickListener(view: View, slotIndex: Int){
        addItemToInventory(view, slotIndex)
    }
    private fun addItemToInventory(view: View, slotIndex: Int){
        var newQuantity = reduceItemQuantityInSpot(slotIndex)
        updateItemQuantityTextView(view, newQuantity)
    }
    private fun reduceItemQuantityInSpot(slotIndex: Int): Int{
        var newQuantity = getItemQuantity(slotIndex) - 1
        setItemQuantity(slotIndex,newQuantity)
        return newQuantity
    }
    private fun setItemQuantity(slotIndex: Int, newQuantity: Int){
        (type as Spot).setItemQuantity(getItem(slotIndex), newQuantity)
        //println((type as Spot).itemsInside[getItem(slotIndex)])
        getItem(slotIndex).printTest()
    }

    private fun addItemImageAndQuantity(slot: View, slotIndex: Int){
        addItemImage(slot, slotIndex)
        addItemQuantity(slot,slotIndex)

    }
    private fun addItemImage(slot: View, slotIndex: Int){
        slot.setBackgroundResource(getItem(slotIndex).imageResource)
    }

    private fun getItem(slotIndex: Int): Item {
        return ArrayList<Item>((type as Spot).itemsInside.keys)[slotIndex]
    }

    private fun addItemQuantity(slot: View, slotIndex: Int){
        var quantity = getItemQuantity(slotIndex)
        updateItemQuantityTextView(slot,quantity)
    }
    private fun getItemQuantity(slotIndex: Int): Int{
        var stringQuantity = ArrayList<String>((type as Spot).itemsInside.values)[slotIndex]
        return parseInt(stringQuantity)
    }

    private fun updateItemQuantityTextView(slot: View, quantity: Int){
        (slot as TextView).text = quantity.toString()
    }
}