package com.example.buriedtownclone

import android.app.Activity
import android.content.Context

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
        (context as Activity).finish()
    }
}