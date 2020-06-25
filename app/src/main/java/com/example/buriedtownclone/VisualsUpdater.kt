package com.example.buriedtownclone

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*

class VisualsUpdater{

    private lateinit var statsBarFragment: StatsBarFragment
    companion object{
        var activity: Activity? = null
    }

    fun showStatsInStatsBar(player: Player){

        if(activity == null){ // If the game handler ended the game
            return
        }
        getStatsBarFragment()
        statsBarFragment.updateStats(player)
    }

    private fun getStatsBarFragment(){

        val fragmentActivity: FragmentActivity = activity as FragmentActivity
        statsBarFragment = fragmentActivity.supportFragmentManager.
        findFragmentByTag("stats bar") as StatsBarFragment
    }

    fun showWalkingPanel(){
        activity!!.walkingPanel.visibility = View.VISIBLE
    }
    fun hideWalkingPanel(){
        activity!!.walkingPanel.visibility = View.GONE
    }
}