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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ItemsFragment(val spot: Spot) : Fragment() {
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

        for (item in spot.itemsInside){
            println(item.key)
        }


        var inflated = setupFragment(inflater, container)
        return inflated
    }

    private fun setupFragment(inflater: LayoutInflater, container: ViewGroup?): View{
        var inflated: GridLayout
        if(spot is Spot){
            inflated = inflater.inflate(R.layout.fragment_items, container, false) as GridLayout
            setupSlotViews(inflated)
        }
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
            slot.setOnClickListener { slotClickListener(slot, getItemToPutInThisSlot(slotsFound)) }
    }
    private fun existsItemToPutInSlot(slotIndex: Int): Boolean{
        return slotIndex < spot.itemsInside.size
    }

    private fun getItemToPutInThisSlot(slotIndex: Int): MutableList<String>{
        var key = ""; var value = ""
        var itemNameAndQuantity: MutableList<String> = mutableListOf()
        key = ArrayList<String>(spot.itemsInside.keys)[slotIndex]
        value = ArrayList<String>(spot.itemsInside.values)[slotIndex]
        itemNameAndQuantity.add(key)
        itemNameAndQuantity.add(value)
        return itemNameAndQuantity
    }

    fun slotClickListener(view: View, item: MutableList<String>){
        println("ITEM ${item[0]}    quantity ${item[1]}")
    }

    private fun addItemImageAndQuantity(slot: TextView, slotIndex: Int){
        addItemImage(slot, slotIndex)
        addItemQuantity(slot,slotIndex)

    }
    private fun addItemImage(slot: TextView, slotIndex: Int){
        slot.setBackgroundResource(stringToSpotImage[getItemName(slotIndex)]!!)
    }
    private fun getItemName(slotIndex: Int): String{
            return getItemToPutInThisSlot(slotIndex)[0]
    }

    private fun addItemQuantity(slot: TextView, slotIndex: Int){
        var quantity = getItemQuantity(slotIndex)
        updateItemQuantity(slot,quantity)
    }
    private fun getItemQuantity(slotIndex: Int): Int{
        return getItemToPutInThisSlot(slotIndex)[1].toInt()
    }

    private fun updateItemQuantity(slot: TextView, quantity: Int){
        slot.text = quantity.toString()
    }
}