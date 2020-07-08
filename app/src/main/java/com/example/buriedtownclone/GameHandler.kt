package com.example.buriedtownclone

import android.app.Activity
import android.content.Intent
import com.example.buriedtownclone.homeequipment.Greenhouse
import com.example.buriedtownclone.homeequipment.Storage

object GameHandler{

    var context: Activity? = null
    var isGameFinished = false

    init {
        isGameFinished = false
    }

    fun endGame(){
        isGameFinished = true
        TimeHandler.stopTimer()
        Database.deleteAllData()
        goToMainMenu()
        freeData()
    }

    private fun goToMainMenu(){
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context!!.startActivity(intent)
    }

    fun freeData(){
        context = null
        VisualsUpdater.activity = null
        TimeHandler.context = null
        VisualsUpdater.dialogFragmentContainer = null
        resetSingletonsData()
    }

    private fun resetSingletonsData(){
        resetEquipments()
    }

    private fun resetEquipments(){
        Greenhouse.reset()
        Storage.reset()
    }
}