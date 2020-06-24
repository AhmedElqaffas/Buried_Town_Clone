package com.example.buriedtownclone

import android.content.Context
import android.os.Handler


class TimeHandler()  {
    var handler = Handler()
    lateinit var thirstDecrease: Runnable
    lateinit var hungerDecrease: Runnable
    lateinit var context: Context
    lateinit var player: Player
    lateinit var database: Database

    var visualUpdater = VisualsUpdater()

    constructor(context: Context, player: Player): this(){
        this.context = context
        this.player = player
        database = Database(context)
    }

    fun startTimer(){
        decreaseThirstEveryFewSeconds()
        decreaseHungerEveryFewSeconds()
    }

    private fun decreaseThirstEveryFewSeconds(){
        thirstDecrease = Runnable {
            decreaseThirst()
            handler.postDelayed(thirstDecrease,GameSettings.rateOfThirst)
        }
        handler.postDelayed(thirstDecrease,GameSettings.rateOfThirst)
    }
    private fun decreaseThirst(){
        player.updateStatsFromDatabase()
        player.updateThirst(-1)
        visualUpdater.showStatsInStatsBar(player)
    }

    private fun decreaseHungerEveryFewSeconds(){
        hungerDecrease = Runnable {
            decreaseHunger()
            handler.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
        }
        handler.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
    }
    private fun decreaseHunger(){
        player.updateStatsFromDatabase()
        player.updateHunger(-1)
        visualUpdater.showStatsInStatsBar(player)
    }

    fun stopTimer(){
        handler.removeCallbacks(thirstDecrease)
        handler.removeCallbacks(hungerDecrease)
        handler = Handler()
    }

    fun updateObjects(context: Context, player: Player){
        this.player = player
        this.context = context
    }

}