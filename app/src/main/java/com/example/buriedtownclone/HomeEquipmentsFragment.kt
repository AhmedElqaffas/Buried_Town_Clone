package com.example.buriedtownclone

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home_equipment.*


class HomeEquipmentsFragment : Fragment(){

    private lateinit var inflated: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflated = inflater.inflate(R.layout.fragment_home_equipment, container, false) as ConstraintLayout
        return inflated
    }

    override fun onResume() {
        super.onResume()
        imageHotspots.setOnTouchListener { v, event ->
            imageHotspots.performClick()
            handleImageClick(v, event)
        }
    }

    private fun handleImageClick(v: View, event: MotionEvent): Boolean{
        val action = event.action
        val eventX: Int = event.x.toInt()
        val eventY: Int = event.y.toInt()

        if(action == MotionEvent.ACTION_DOWN){
            checkClickedColor(eventX, eventY)
        }

        if(action == MotionEvent.ACTION_UP){
            println("Action up")
        }
        return true
    }

    private fun checkClickedColor(eventX: Int, eventY: Int){
        val touchColor = getHotspotColor (eventX, eventY)
        when {
            doColorsMatch(Color.GREEN, touchColor) -> {
                handleGreenhouseClicked()
            }
            doColorsMatch(Color.WHITE, touchColor) -> {
                handleBedClicked()
            }
            doColorsMatch(Color.parseColor("#707070"), touchColor) -> {
                handleStorageClicked()
            }
            else -> {
                println("None")
            }
        }
    }
    /**
       Description: Gets the color of the pixel touched
     */
    private fun getHotspotColor(x: Int, y: Int): Int {
        val image = imageHotspots
        image.isDrawingCacheEnabled = true
        val hotspots: Bitmap = Bitmap.createBitmap(image.drawingCache)
        image.isDrawingCacheEnabled = false
        return hotspots.getPixel(x, y)
    }

    /**
       Description: Colors may differ a little from a screen to another so we use a tolerance to check whether
        the color clicked is nearly one of the anticipated colors or not
     */
    private fun doColorsMatch(anticipatedColor: Int, touchedColor: Int): Boolean {
        val tolerance = 25
        return !(kotlin.math.abs(Color.red(anticipatedColor) - Color.red(touchedColor)) > tolerance
            || kotlin.math.abs(Color.green(anticipatedColor) - Color.green(touchedColor)) > tolerance
            || kotlin.math.abs(Color.blue(anticipatedColor) - Color.blue(touchedColor)) > tolerance)
    }

    private fun handleGreenhouseClicked(){
        val intent = Intent(context, EquipmentActivity::class.java)
        intent.putExtra("equipment", HomeSpot.getEquipment("Greenhouse"))
        startActivity(intent)
    }

    private fun handleBedClicked(){
        val intent = Intent(context, EquipmentActivity::class.java)
        intent.putExtra("equipment", HomeSpot.getEquipment("Bed"))
        startActivity(intent)
    }

    private fun handleStorageClicked(){
        val intent = Intent(context, EquipmentActivity::class.java)
        intent.putExtra("equipment", HomeSpot.getEquipment("Storage"))
        startActivity(intent)
    }

}