package com.example.buriedtownclone

import android.content.Context

class Player(val context: Context) {
    var hp: Int = 0
    var hunger: Int = 0
    var thirst: Int = 0

    var database: Database = Database(context)

    init{
        hp = database.getHealthPoints()!!
        hunger = database.getHunger()
        thirst = database.getThirst()
    }

    fun getPlayerStats(): Array<Int>{
        return arrayOf(hp,hunger,thirst)
    }

}