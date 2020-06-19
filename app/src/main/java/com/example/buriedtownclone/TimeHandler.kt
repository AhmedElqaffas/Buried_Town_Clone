package com.example.buriedtownclone

import android.content.Context
import android.os.Handler


class TimeHandler(val context: Context, val player: Player)  {
    var handler = Handler()
    lateinit var thirstDecrease: Runnable
    lateinit var hungerDecrease: Runnable

    var database = Database(context)

    var visualUpdater = VisualsUpdater()

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
        var playerThirst = (player.getThirst()) - 1
        player.setThirst(playerThirst)
        database.setThirst(playerThirst)
        visualUpdater.updateThirst(playerThirst)
    }

    private fun decreaseHungerEveryFewSeconds(){
        hungerDecrease = Runnable {
            decreaseHunger()
            handler.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
        }
        handler.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
    }
    private fun decreaseHunger(){
        var playerHunger = (player.getHunger()) - 1
        player.setHunger(playerHunger)
        database.setHunger(playerHunger)
        visualUpdater.updateHunger(playerHunger)

    }

    fun stopTimer(){
        handler.removeCallbacks(thirstDecrease)
        handler.removeCallbacks(hungerDecrease)
    }

}