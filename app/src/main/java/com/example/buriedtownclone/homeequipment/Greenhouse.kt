package com.example.buriedtownclone.homeequipment

import androidx.fragment.app.Fragment
import com.example.buriedtownclone.Definitions
import com.example.buriedtownclone.GreenhouseFragment

class Greenhouse: Equipment("Greenhouse", 1, Definitions.greenhouseDescription) {

    override fun getFragment(): Fragment {
        return GreenhouseFragment()
    }
}