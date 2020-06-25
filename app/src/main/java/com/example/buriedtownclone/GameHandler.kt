package com.example.buriedtownclone

import android.app.Activity
import android.content.Context
import android.content.Intent

class GameHandler() {
    companion object{
        lateinit var context: Activity
        lateinit var database: Database
    }

    constructor(activity: Activity) : this() {
        context = activity
        database = Database(context)
    }

    fun endGame(){
        (context as GameActivity).timeHandler.stopTimer()
        handleGameEndingWhenNavigatingCase()
        database.deleteAllData()
        goToMainMenu()
    }
    private fun goToMainMenu(){
        var intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }

    private fun handleGameEndingWhenNavigatingCase(){
        (context as GameActivity).handler.removeCallbacksAndMessages(null)
        (context as GameActivity).stopSound()
    }
}