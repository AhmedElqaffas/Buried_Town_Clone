package com.example.buriedtownclone

import android.app.Activity
import android.content.Intent

class GameHandler {



    companion object{

        var context: Activity? = null
        private var database = Database()
        private var timeHandler = TimeHandler()
        var isGameFinished = false
    }

    constructor(){
        isGameFinished = false
    }

    fun endGame(){
        isGameFinished = true
        timeHandler.stopTimer()
        database.deleteAllData()
        goToMainMenu()
        freeData()
    }
    private fun goToMainMenu(){
        println(context.toString())
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context!!.startActivity(intent)
    }

    private fun freeData(){
        context = null
        VisualsUpdater.activity = null
        TimeHandler.context = null
    }
}