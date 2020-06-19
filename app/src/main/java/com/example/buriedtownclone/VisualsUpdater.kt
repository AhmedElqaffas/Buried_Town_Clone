package com.example.buriedtownclone

import android.app.Activity
import android.graphics.Color
import android.opengl.Visibility
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_game.*


class VisualsUpdater() {
    companion object{
        lateinit var activity: Activity
    }

    constructor(activity: Activity) : this() {
        VisualsUpdater.activity = activity
    }
    fun updateHunger(value: Int){
        activity.hungerTextView.text = value.toString()
    }
    fun updateThirst(value: Int){
        activity.thirstTextView.text = value.toString()
    }
    fun updateHealthPoints(value: Int){
        activity.hpTextView.text = value.toString()
    }

    fun showWalkingPanel(){
        activity.walkingPanel.visibility = View.VISIBLE
    }
    fun hideWalkingPanel(){
        activity.walkingPanel.visibility = View.GONE
    }

}