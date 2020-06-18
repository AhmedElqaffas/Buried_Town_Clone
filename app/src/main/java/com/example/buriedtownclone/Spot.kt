package com.example.buriedtownclone

class Spot {
    var spotType: String = ""
    var cityX: Int = 0
    var cityY: Int = 0
    var locationWithinCity: Int = 0
    var itemsInside: HashMap<String,String> = HashMap()
    var visited: Boolean = false
}