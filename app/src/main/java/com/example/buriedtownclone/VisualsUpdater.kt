package com.example.buriedtownclone

import android.R
import android.app.Activity
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
}