package com.example.buriedtownclone

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HorizontalFragmentsAdapter (val fragment: FragmentManager, lifeCycle: Lifecycle): FragmentStateAdapter(fragment,lifeCycle) {

    override fun getItemCount(): Int {
        return GameSettings.horizontalSize
    }

    override fun createFragment(position: Int): Fragment {
        println("swiped, position $position")
        return HorizontalMovementFragment()

    }
}