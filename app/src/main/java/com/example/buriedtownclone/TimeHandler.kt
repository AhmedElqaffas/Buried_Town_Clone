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
        player.thirst--
        database.setThirst(player.thirst)
        visualUpdater.updateThirst(player.thirst)
    }

    private fun decreaseHungerEveryFewSeconds(){
        hungerDecrease = Runnable {
            decreaseHunger()
            handler.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
        }
        handler.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
    }
    private fun decreaseHunger(){
        player.hunger--
        database.setHunger(player.hunger)
        visualUpdater.updateHunger(player.hunger)

    }

    fun stopTimer(){
        handler.removeCallbacks(thirstDecrease)
        handler.removeCallbacks(hungerDecrease)
    }

}