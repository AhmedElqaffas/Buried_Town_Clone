package com.example.buriedtownclone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_stats_bar.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

var visualsUpdater = VisualsUpdater()

class StatsBarFragment(val player: Player) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_stats_bar, container, false)
    }

    override fun onResume() {
        super.onResume()
        updateStats(player)
        showStats()
    }

    fun updateStats(player: Player){
        hpTextView.text = player.getHealthPoints().toString()
        hungerTextView.text = player.getHunger().toString()
        thirstTextView.text = player.getThirst().toString()
    }

    private fun showStats(){
        visualsUpdater.showStatsInStatsBar(player)
    }

}