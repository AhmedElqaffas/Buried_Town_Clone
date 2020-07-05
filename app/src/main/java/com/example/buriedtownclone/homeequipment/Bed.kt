package com.example.buriedtownclone.homeequipment

import androidx.fragment.app.Fragment
import com.example.buriedtownclone.BedFragment
import com.example.buriedtownclone.Definitions
import com.example.buriedtownclone.GreenhouseFragment

object Bed: Equipment("Bed", 1, Definitions.bedDescription) {

    override fun getFragment(): Fragment {
        return BedFragment()
    }

    override fun upgrade(): Boolean {
        return false
    }

    override fun performAction(): Boolean {
        TODO("Not yet implemented")
    }
}