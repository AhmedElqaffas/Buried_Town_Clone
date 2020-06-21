package com.example.buriedtownclone

import android.app.Activity
import android.content.Context
import android.content.Intent

class GameHandler() {
    companion object{
        lateinit var context: Context
        lateinit var database: Database
    }

    constructor(context: Context) : this() {
        GameHandler.context = context
        database = Database(Companion.context)
    }

    fun endGame(){
        database.deleteAllData()
        goToMainMenu()
    }
    private fun goToMainMenu(){
        var intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
    }
}