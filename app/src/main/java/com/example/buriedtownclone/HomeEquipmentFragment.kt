package com.example.buriedtownclone

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import at.lukle.clickableareasimage.ClickableArea
import at.lukle.clickableareasimage.ClickableAreasImage
import at.lukle.clickableareasimage.OnClickableAreaClickedListener
import kotlinx.android.synthetic.main.fragment_home_equipment.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.lang.Math.abs


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeEquipmentFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var inflated: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
            val touchColor = getHotspotColor (eventX, eventY)
            val tolerance = 25
            if(closeMatch (Color.GREEN, touchColor, tolerance)){
                println("GREENHOUSE TOUCHED")
            } else if(closeMatch (Color.WHITE, touchColor, tolerance)){
                println("BED TOUCHED")
            } else{
                println("None")
            }
        }

        if(action == MotionEvent.ACTION_UP){
            println("Action up")
        }
        return true
    }
    private fun getHotspotColor(x: Int, y: Int): Int {
        val image = imageHotspots
        image.isDrawingCacheEnabled = true
        val hotspots: Bitmap = Bitmap.createBitmap(image.getDrawingCache())
        image.setDrawingCacheEnabled(false)
        return hotspots.getPixel(x, y)
    }

    private fun closeMatch(color1: Int, color2: Int, tolerance: Int): Boolean {
        if (abs(Color.red(color1) - Color.red(color2)) > tolerance) {
            return false
        }
        if (abs(Color.green(color1) - Color.green(color2)) > tolerance) {
            return false
        }
        if (abs(Color.blue(color1) - Color.blue(color2)) > tolerance) {
            return false
        }
        return true
    }






}