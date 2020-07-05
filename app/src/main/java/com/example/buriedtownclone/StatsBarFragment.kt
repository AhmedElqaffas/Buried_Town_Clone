package com.example.buriedtownclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_stats_bar.*

class StatsBarFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats_bar, container, false)
    }

    override fun onResume() {
        super.onResume()
        updateStats()
        showStats()
    }

    fun updateStats(){
        hpTextView.text = Player.getHealthPoints().toString()
        hungerTextView.text = Player.getHunger().toString()
        thirstTextView.text = Player.getThirst().toString()
    }

    private fun showStats(){
        VisualsUpdater.showStatsInStatsBar()
    }

}