package com.example.buriedtownclone

import android.content.Context
import android.os.Handler


object TimeHandler{

    private var handler: Handler? = null

    private lateinit var thirstDecrease: Runnable
    private lateinit var hungerDecrease: Runnable
    private lateinit var navigationThirstDecrease: Runnable
    private lateinit var navigationHungerDecrease: Runnable
    var context: Context? = null
    private var stopped = true



    fun startTimer(){
        handler = Handler()
        decreaseThirstEveryFewSeconds()
        decreaseHungerEveryFewSeconds()
        stopped = false
    }

    private fun decreaseThirstEveryFewSeconds(){
        thirstDecrease = Runnable {
            decreaseThirst()
            if(Player.getThirst() <= 0){
                return@Runnable
            }
            handler!!.postDelayed(thirstDecrease,GameSettings.rateOfThirst)
        }
        handler!!.postDelayed(thirstDecrease,GameSettings.rateOfThirst)
    }
    private fun decreaseThirst(){
        Player.updateThirst(-1)
        //visualUpdater.showStatsInStatsBar(player)
    }

    private fun decreaseHungerEveryFewSeconds(){
        hungerDecrease = Runnable {
            decreaseHunger()
            if(Player.getHunger() <= 0){
                return@Runnable
            }
            handler!!.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
        }
        handler!!.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
    }
    private fun decreaseHunger(){

        Player.updateHunger(-1)
        //visualUpdater.showStatsInStatsBar(player)
    }

    fun stopTimer(){
        handler!!.removeCallbacks(thirstDecrease)
        handler!!.removeCallbacks(hungerDecrease)
        stopped = true
    }

    fun startNavigationStatsDecrease(){
        acceleratedThirstDecrease()
        acceleratedHungerDecrease()
    }
    private fun acceleratedThirstDecrease(){
        navigationThirstDecrease = Runnable {
            decreaseThirst()
            if(Player.getThirst() <= 0){
                return@Runnable
            }
            handler!!.postDelayed(navigationThirstDecrease,GameSettings.navigationRateOfThirst)
        }
        handler!!.postDelayed(navigationThirstDecrease,GameSettings.navigationRateOfThirst)
    }

    private fun acceleratedHungerDecrease(){
        navigationHungerDecrease = Runnable {
            decreaseHunger()
            if(Player.getHunger() <= 0){
                return@Runnable
            }
            handler!!.postDelayed(navigationHungerDecrease,GameSettings.navigationRateOfHunger)
        }
        handler!!.postDelayed(navigationHungerDecrease,GameSettings.navigationRateOfHunger)
    }

    fun stopNavigationStatsDecrease(){
        handler!!.removeCallbacks(navigationHungerDecrease)
        handler!!.removeCallbacks(navigationThirstDecrease)
    }

    fun isStopped(): Boolean{
        return stopped
    }

}