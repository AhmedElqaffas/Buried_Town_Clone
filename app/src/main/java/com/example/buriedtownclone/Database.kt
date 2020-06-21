package com.example.buriedtownclone

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Database(){

    companion object{
        lateinit var context: Context
        lateinit  var database: SQLiteDatabase
    }

    constructor(context: Context): this(){
        Database.context = context
    }

    fun initializeDatabase(){
        openOrCreateDatabase()
        createTables()
    }
    fun openOrCreateDatabase(){
        database = context.openOrCreateDatabase("Game", Context.MODE_PRIVATE, null)
    }
    private fun createTables(){
        createStatsTable()
        createWeaponsTable()
        createAmmoTable()
        createFoodTable()
        createDrinksTable()
        createCitiesTable()
        createSpotsTable()
    }
    private fun createStatsTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS stats(stat VARCHAR(50), " +
                "quantity TINYINT UNSIGNED, CONSTRAINT pk PRIMARY KEY (stat))")
    }
    private fun createWeaponsTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS weapons(weapon_name VARCHAR(100), " +
                 "quantity TINYINT UNSIGNED, CONSTRAINT pk PRIMARY KEY (weapon_name))")
    }
    private fun createAmmoTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS ammo(ammo_name VARCHAR(100), " +
                "quantity INT UNSIGNED, CONSTRAINT pk PRIMARY KEY (ammo_name))")
    }
    private fun createFoodTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS food(food_name VARCHAR(100), " +
                "quantity INT UNSIGNED, CONSTRAINT pk PRIMARY KEY (food_name))")
    }
    private fun createDrinksTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS drinks(drink_name VARCHAR(100), " +
                "quantity INT UNSIGNED, CONSTRAINT pk PRIMARY KEY (drink_name))")
    }
    private fun createCitiesTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS cities(x_position TINYINT, " +
                "y_position TINYINT, CONSTRAINT pk PRIMARY KEY (x_position, y_position))")
    }
    private fun createSpotsTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS spots(index_within_city TINYINT,city_x TINYINT," +
                " city_y TINYINT, inner_items_map TEXT, visited INTEGER DEFAULT 0, type VARCHAR(50)," +
                "  CONSTRAINT pk PRIMARY KEY " +
                "(index_within_city, city_x, city_y), " +
                "CONSTRAINT fk FOREIGN KEY(city_x, city_y) REFERENCES cities(x_position, y_position))")
    }
    private fun getQuantityQuery(tableName: String, selected: String, selector: String): String{
        return "SELECT quantity FROM $tableName WHERE $selector='$selected'"
    }

    fun deleteAllData(){
        database.execSQL("DELETE FROM stats")
        database.execSQL("DELETE FROM weapons")
        database.execSQL("DELETE FROM ammo")
        database.execSQL("DELETE FROM food")
        database.execSQL("DELETE FROM drinks")
        database.execSQL("DELETE FROM cities")
        database.execSQL("DELETE FROM spots")
    }

    fun initializeStats(){
        database.execSQL("INSERT INTO stats values('hp',100)" )
        database.execSQL("INSERT INTO stats values('hunger',100)")
        database.execSQL("INSERT INTO stats values('thirst',100)")
        database.execSQL("INSERT INTO stats values('x_position',0)")
        database.execSQL("INSERT INTO stats values('y_position',0)")
    }

    fun getHealthPoints(): Int?{
        var healthPointsQuery: Cursor = database.rawQuery(getQuantityQuery
            ("stats","hp","stat"), null)
        return if(!foundResults(healthPointsQuery)){
            null
        } else {
            // Note that foundResults already executed the .moveToFirst()
            healthPointsQuery.getInt(0)
        }
    }
    private fun foundResults(query: Cursor): Boolean{
        return query.moveToFirst()
    }

    fun getHunger(): Int{
        var hungerQuery: Cursor = database.rawQuery(getQuantityQuery
            ("stats","hunger","stat"), null)
            hungerQuery.moveToFirst()
            return hungerQuery.getInt(0)
    }
    fun getThirst(): Int{
        var thirstQuery: Cursor = database.rawQuery(getQuantityQuery
            ("stats","thirst","stat"), null)
        thirstQuery.moveToFirst()
        return thirstQuery.getInt(0)
    }

    fun saveCity(locationX: Int, locationY: Int){
        database.execSQL("INSERT INTO cities values($locationX,$locationY)")
    }

    fun getSpotsInCity(city: City): MutableList<Spot>{
        var spotsQueryResult: Cursor = database.rawQuery(getSpotsQuery(city), null)
        return formSpotsObjects(spotsQueryResult)
    }
    private fun getSpotsQuery(city: City): String{
        return "SELECT * FROM spots WHERE city_x = ${city.locationX} " +
                "and city_y = ${city.locationY}"
    }
    private fun formSpotsObjects(results: Cursor): MutableList<Spot>{
        var spotsObjects: MutableList<Spot> = mutableListOf()
        results.moveToFirst()
        while(!results.isAfterLast){
            spotsObjects.add(formSpot(results))
            results.moveToNext()
        }
        return spotsObjects
    }
    private fun formSpot(query: Cursor): Spot{

        var spot = Spot()
        spot.cityX = query.getInt(query.getColumnIndex("city_x"))
        spot.cityY = query.getInt(query.getColumnIndex("city_y"))
        spot.locationWithinCity = query.getInt(query.getColumnIndex("index_within_city"))
        spot.visited = query.getInt(query.getColumnIndex("visited")) == 1 // to convert int to boolean
        spot.itemsInside = unserializeItemsMap(query)
        spot.spotType = query.getString(query.getColumnIndex("type"))
        return spot
    }
    private fun unserializeItemsMap(query: Cursor): LinkedHashMap<String, String>{
        var itemsInsideHashMap = linkedMapOf<String, String>()
        var itemsInsideSpotText = query.getString(query.getColumnIndex("inner_items_map"))
        try {
            val json = JSONObject(itemsInsideSpotText)
            val names: JSONArray = json.names()
            for (i in 0 until names.length()) {
                val key = names.getString(i)
                itemsInsideHashMap[key] = json.opt(key).toString()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return itemsInsideHashMap
    }

    fun saveSpot(spot: Spot){
        val gson = Gson()
        val isVisitedInt = if (spot.visited) 1 else 0
        val itemsInsideText: String = gson.toJson(spot.itemsInside)
        database.execSQL("INSERT INTO spots values(${spot.locationWithinCity}," +
                "${spot.cityX}, ${spot.cityY},'$itemsInsideText', ${isVisitedInt}, '${spot.spotType}')")
    }

    fun updateSpotVisit(spot: Spot){
        database.execSQL("UPDATE spots SET visited = 1 " +
                "WHERE index_within_city = ${spot.locationWithinCity}" +
                " and city_x = ${spot.cityX} and city_y = ${spot.cityY}")
    }

    fun setThirst(value: Int){
        database.execSQL("UPDATE stats SET quantity = $value WHERE stat = 'thirst'")
    }

    fun setHunger(value: Int){
        database.execSQL("UPDATE stats SET quantity = $value WHERE stat = 'hunger'")
    }

    fun getCities(): MutableList<City>{
        var citiesQueryResult: Cursor = database.rawQuery(getCitiesQuery(), null)
        return formCityResultsIntoObjects(citiesQueryResult)
    }
    private fun getCitiesQuery(): String{
        return "SELECT * FROM cities"
    }
    private fun formCityResultsIntoObjects(results: Cursor): MutableList<City>{
        var cityObjects = mutableListOf<City>()
        results.moveToFirst()
        while(!results.isAfterLast){
            cityObjects.add(formCity(results))
            results.moveToNext()
        }
        return cityObjects
    }
    private fun formCity(result: Cursor): City{
        var locationX = result.getInt(result.getColumnIndex("x_position"))
        var locationY = result.getInt(result.getColumnIndex("y_position"))
        var city = City(locationX,locationY)
        formSpotsWithinCity(city)
        return city
    }
    private fun formSpotsWithinCity(city: City){
        var spotsInCity = getSpotsInCity(city)
        city.numberOfSpotsWithin = spotsInCity.size
        for (spot in spotsInCity){
            city.spots.add(spot)
        }
    }

    fun getPlayerLocation(): MutableList<Int>{
        var locationQueryResult: Cursor = database.rawQuery(getLocationQuery(), null)
        return extractLocationFromQuery(locationQueryResult)
    }
    private fun getLocationQuery(): String{
        return "SELECT quantity FROM stats WHERE stat = 'x_position' or stat = 'y_position'"
    }
    private fun extractLocationFromQuery(query: Cursor): MutableList<Int>{
        var locationList = mutableListOf<Int>()
        query.moveToFirst()
        locationList.add(query.getInt(query.getColumnIndex("quantity")))
        query.moveToNext()
        locationList.add(query.getInt(query.getColumnIndex("quantity")))
        return locationList
    }

    fun updatePlayerLocation(x: Int, y: Int){
        database.execSQL("UPDATE stats SET quantity = $x WHERE stat = 'x_position'")
        database.execSQL("UPDATE stats SET quantity = $y WHERE stat = 'y_position'")
    }

    fun dropAllTables(){
        database.execSQL("DROP TABLE stats")
        database.execSQL("DROP TABLE weapons")
        database.execSQL("DROP TABLE ammo")
        database.execSQL("DROP TABLE food")
        database.execSQL("DROP TABLE drinks")
        database.execSQL("DROP TABLE cities")
        database.execSQL("DROP TABLE spots")

    }

}