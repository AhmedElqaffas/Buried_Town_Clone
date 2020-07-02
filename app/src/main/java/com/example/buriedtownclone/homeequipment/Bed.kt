package com.example.buriedtownclone.homeequipment

import androidx.fragment.app.Fragment
import com.example.buriedtownclone.BedFragment
import com.example.buriedtownclone.Definitions
import com.example.buriedtownclone.GreenhouseFragment

class Bed: Equipment("Bed", 1, Definitions.bedDescription) {

    override fun getFragment(): Fragment {
        return BedFragment()
    }
}