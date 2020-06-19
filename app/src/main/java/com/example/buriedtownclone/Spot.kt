package com.example.buriedtownclone

import java.io.Serializable

/* Implements serializable to be able to send an object of this class to the inventory activity
   using the put extra, as put extra doesn't allow sending normal objects
 */
class Spot: Serializable{
    var spotType: String = ""
    var cityX: Int = 0
    var cityY: Int = 0
    var locationWithinCity: Int = 0
    var itemsInside: HashMap<String,String> = HashMap()
    var visited: Boolean = false
}