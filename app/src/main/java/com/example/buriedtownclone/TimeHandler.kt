package com.example.buriedtownclone

import android.content.Context
import android.os.Handler


class TimeHandler{
    companion object{

        var handler: Handler? = null

        lateinit var thirstDecrease: Runnable
        lateinit var hungerDecrease: Runnable
        lateinit var navigationThirstDecrease: Runnable
        lateinit var navigationHungerDecrease: Runnable
        var context: Context? = null
        private var stopped = true
        private var player = Player()
        private var visualUpdater = VisualsUpdater()
    }



    fun startTimer(){
        handler = Handler()
        decreaseThirstEveryFewSeconds()
        decreaseHungerEveryFewSeconds()
        stopped = false
    }

    private fun decreaseThirstEveryFewSeconds(){
        thirstDecrease = Runnable {
            decreaseThirst()
            if(player.getThirst() <= 0){
                return@Runnable
            }
            handler!!.postDelayed(thirstDecrease,GameSettings.rateOfThirst)
        }
        handler!!.postDelayed(thirstDecrease,GameSettings.rateOfThirst)
    }
    private fun decreaseThirst(){
        player.updateThirst(-1)
        //visualUpdater.showStatsInStatsBar(player)
    }

    private fun decreaseHungerEveryFewSeconds(){
        hungerDecrease = Runnable {
            decreaseHunger()
            if(player.getHunger() <= 0){
                return@Runnable
            }
            handler!!.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
        }
        handler!!.postDelayed(hungerDecrease,GameSettings.rateOfHunger)
    }
    private fun decreaseHunger(){

        player.updateHunger(-1)
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
            if(player.getThirst() <= 0){
                return@Runnable
            }
            handler!!.postDelayed(navigationThirstDecrease,GameSettings.navigationRateOfThirst)
        }
        handler!!.postDelayed(navigationThirstDecrease,GameSettings.navigationRateOfThirst)
    }

    private fun acceleratedHungerDecrease(){
        navigationHungerDecrease = Runnable {
            decreaseHunger()
            if(player.getHunger() <= 0){
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