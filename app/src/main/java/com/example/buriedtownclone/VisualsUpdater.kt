package com.example.buriedtownclone

import android.app.Activity
import android.graphics.Color
import android.opengl.Visibility
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_spot.*
import kotlinx.android.synthetic.main.fragment_stats_bar.*


class VisualsUpdater() {

    lateinit var statsBarFragment: StatsBarFragment
    companion object{
        lateinit var activity: Activity
    }

    constructor(activity: Activity) : this() {
        VisualsUpdater.activity = activity
    }

    private fun getStatsBarFragment(){
        val fragmentActivity: FragmentActivity = activity as FragmentActivity
        statsBarFragment = fragmentActivity.supportFragmentManager.
        findFragmentByTag("stats bar") as StatsBarFragment
    }

    fun showStatsInStatsBar(player: Player){
        getStatsBarFragment()
        statsBarFragment.updateStats(player)
    }

    fun showWalkingPanel(){
        activity.walkingPanel.visibility = View.VISIBLE
    }
    fun hideWalkingPanel(){
        activity.walkingPanel.visibility = View.GONE
    }

    fun updateActivityContext(activity: Activity){
        VisualsUpdater.activity = activity
    }


}