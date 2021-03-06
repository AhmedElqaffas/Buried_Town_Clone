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
import com.example.buriedtownclone.homeequipment.Storage
import com.google.common.collect.LinkedListMultimap
import java.lang.Integer.parseInt



class ItemsFragment(private val itemContainerType: ItemsContainer) : Fragment() {

    private lateinit var inflated: GridLayout
    private var itemActionDecider : ItemActionDecider? = null

    interface ItemActionDecider{
        fun onItemClicked(item: Item, slotsFound: Int, itemsFragment: ItemsFragment){
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        itemActionDecider = context as ItemActionDecider
    }

    override fun onDetach() {
        super.onDetach()
        itemActionDecider = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setupFragment(inflater, container)
        return inflated
    }

    private fun setupFragment(inflater: LayoutInflater, container: ViewGroup?): View{

         inflated = inflater.inflate(R.layout.fragment_items, container, false)
                as GridLayout

        renameFragmentTitle()
        drawSlots()
        setupSlotViews()

        return inflated
    }

    private fun renameFragmentTitle(){
        val titleTextView = inflated.findViewById<TextView>(R.id.fragmentTitle)
        titleTextView.text = itemContainerType.getClassName()
    }

    private fun drawSlots(){
        for(i in 0 until itemContainerType.slots){
            val slot = drawCurrentSlot()
            customizeCurrentSlot(slot)
        }
    }
    private fun drawCurrentSlot(): TextView{
        val newSlot = TextView(context)
        inflated.addView(newSlot)
        return newSlot
    }
    private fun customizeCurrentSlot(slot: TextView){
        setSlotTag(slot)
        setSlotWeightInParent(slot)
        customizeTextStyle(slot)
        setSlotBackground(slot)
    }
    private fun setSlotTag(slot: View){
        val indexWithinParent: Int = (slot.parent as GridLayout).indexOfChild(slot)
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
    private fun setSlotBackground(slot: TextView){
        slot.setBackgroundResource(R.drawable.empty_slot)
    }

    private fun setupSlotViews(){
        var slotsFound = 0
        for(slot in inflated.children){
            if(existsItemToPutInSlot(slotsFound) && slot.id != R.id.fragmentTitle){
                addListener(slot as TextView, slotsFound)
                addItemImageAndQuantity(slot, slotsFound)
                slotsFound++
            }
        }
    }
    private fun existsItemToPutInSlot(slotIndex: Int): Boolean{
        return slotIndex < itemContainerType.itemsInside.size()
    }

    private fun addListener(slot: TextView, slotsFound: Int){
        slot.setOnClickListener { itemActionDecider!!.onItemClicked(getItem(slotsFound), slotsFound, this) }
    }

    fun consumeItem(item: Item, slotIndex: Int){
        if(item.isConsumable()){
            //player.updateStatsFromDatabase()
            reduceItemQuantity(slotIndex)
            item.activateItemEffect()
        }
    }

    fun addItem(item: Item): Boolean{
        val itemAdded = itemContainerType.addItem(item)
        if(itemAdded){
            refreshLayout()
            return true
        }
        return false
    }

    fun removeItem(slotIndex: Int){
        reduceItemQuantity(slotIndex)
    }

    private fun reduceItemQuantity(slotIndex: Int){
        itemContainerType.decrementClickedItemQuantity(slotIndex)
        refreshLayout()
    }

    private fun refreshLayout(){
        resetSlots()
        setupSlotViews()
    }
    private fun resetSlots(){
        for(slot in inflated.children){
            if(slot.id != R.id.fragmentTitle){
                (slot as TextView).text = ""
                slot.setBackgroundResource(R.drawable.empty_slot)
                slot.setOnClickListener{}
            }
        }
    }

    private fun addItemImageAndQuantity(slot: View, slotIndex: Int){
        addItemImage(slot, slotIndex)
        addItemQuantity(slotIndex)

    }
    private fun addItemImage(slot: View, slotIndex: Int){
        slot.setBackgroundResource(getItem(slotIndex).imageResource)
    }

    private fun getItem(slotIndex: Int): Item {
        return itemContainerType.getItemAt(slotIndex)
    }

    private fun addItemQuantity(slotIndex: Int){
        val quantity = getItemQuantity(slotIndex)
        updateItemQuantityTextView(slotIndex,quantity)
    }
    private fun getItemQuantity(slotIndex: Int): Int{
        val stringQuantity = itemContainerType.getQuantityOfItemAt(slotIndex)
        return parseInt(stringQuantity)
    }

    private fun updateItemQuantityTextView(slotIndex: Int, quantity: Int){
        val slotView = inflated.getChildAt(slotIndex + 1) as TextView
        slotView.text = quantity.toString()
    }

    fun getItemsMap(): LinkedListMultimap<Item, String> {
        return itemContainerType.itemsInside
    }
}