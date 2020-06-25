package com.example.buriedtownclone

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class Database(){

    companion object{
        lateinit var context: Context
        lateinit var database: SQLiteDatabase
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
        createHomeSpotTable()
    }
    private fun createStatsTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS stats(stat VARCHAR(50), " +
                "quantity Text, CONSTRAINT pk PRIMARY KEY (stat))")
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
    private fun createHomeSpotTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS home(type_home VARCHAR(50), farm_level TINYINT, " +
                "CONSTRAINT pk PRIMARY KEY (type_home), " +
                "CONSTRAINT home_foreign_key FOREIGN KEY (type_home) REFERENCES spots(type))")
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
        database.execSQL("DELETE FROM home")
    }

    fun initializeStats(){
        database.execSQL("INSERT INTO stats values('hp','100')" )
        database.execSQL("INSERT INTO stats values('hunger','100')")
        database.execSQL("INSERT INTO stats values('thirst','100')")
        database.execSQL("INSERT INTO stats values('x_position','0')")
        database.execSQL("INSERT INTO stats values('y_position','0')")
        database.execSQL("Insert Into stats values('inventory','')")
    }

    fun getHealthPoints(): Int?{
        val healthPointsQuery: Cursor = database.rawQuery(getQuantityQuery
            ("stats","hp","stat"), null)
        return if(!foundResults(healthPointsQuery)){
            null
        } else {
            // Note that foundResults already executed the .moveToFirst()
            healthPointsQuery.getString(0).toInt()
        }
    }
    private fun foundResults(query: Cursor): Boolean{
        return query.moveToFirst()
    }

    fun getHunger(): Int{
        var hungerQuery: Cursor = database.rawQuery(getQuantityQuery
            ("stats","hunger","stat"), null)
            hungerQuery.moveToFirst()
            return hungerQuery.getString(0).toInt()
    }
    fun getThirst(): Int{
        var thirstQuery: Cursor = database.rawQuery(getQuantityQuery
            ("stats","thirst","stat"), null)
        thirstQuery.moveToFirst()
        return thirstQuery.getString(0).toInt()
    }

    fun saveCity(locationX: Int, locationY: Int){
        database.execSQL("INSERT INTO cities values($locationX,$locationY)")
    }

    fun getSpotsInCity(city: City): MutableList<Spot>{
        var spotsQueryResult: Cursor = database.rawQuery(getSpotsQuery(city), null)
        return formSpotsObjects(spotsQueryResult)
    }
    private fun getSpotsQuery(city: City): String{
        return "SELECT * FROM spots LEFT JOIN home ON spots.type = home.type_home WHERE city_x = ${city.locationX} " +
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
    private fun formSpot(queryRow: Cursor): Spot{

        var spot: Spot
        val spotType = queryRow.getString(queryRow.getColumnIndex("type"))
        println(spotType)
        spot = if(spotType == Definitions.home){
            HomeSpot()
        } else{
            NormalSpot()
        }
        spot.formObject(queryRow)
        return spot
    }
    fun unserializeItemsMap(query: Cursor): LinkedHashMap<Item, String>{
        var itemsInsideHashMap = linkedMapOf<Item, String>()
        var itemsInsideSpotText = getItemMapString(query)
        try {
            val json = JSONObject(itemsInsideSpotText)
            val names: JSONArray = json.names()
            for (i in 0 until names.length()) {
                val key = names.getString(i)
                itemsInsideHashMap.put(Class.forName(key).newInstance() as Item, json.opt(key).toString())
            }
        } catch (e: Exception) {
            return itemsInsideHashMap
        }

        return itemsInsideHashMap

    }
    /* If this is a stats table query, it gets the 'quantity' field
       If this is a spots table query, it gets the 'inner_items_map' field
     */
    private fun getItemMapString(query: Cursor): String{
        return if(query.getColumnIndex("quantity") != -1){
            query.getString(query.getColumnIndex("quantity"))
        }else{
            query.getString(query.getColumnIndex("inner_items_map"))
        }


    }

    fun saveHomeSpot(homeSpot: HomeSpot){
        saveSpot(homeSpot)
        addHomeDetails(homeSpot)
    }
    fun saveSpot(spot: Spot){
        val isVisitedInt = if (spot.visited) 1 else 0
        val itemsInsideText: String = serializeItemMap(spot.itemsInside)
        database.execSQL("INSERT INTO spots values(${spot.locationWithinCity}," +
                "${spot.cityX}, ${spot.cityY},'$itemsInsideText', ${isVisitedInt}, '${spot.spotType}')")
    }
    private fun addHomeDetails(homeSpot: HomeSpot){
        database.execSQL("INSERT INTO home values('${homeSpot.spotType}'," +
                "${homeSpot.farmLevel})")
    }
    private fun serializeItemMap(itemMap: LinkedHashMap<Item,String>): String{
        var serializedMap = linkedMapOf<String, String>()
        for(entry in itemMap){
            var serializedEntry = serializeEntry(entry)
            serializedMap.put(serializedEntry[0], serializedEntry[1])
        }
        val gson = Gson()
        return gson.toJson(serializedMap)
    }
    private fun serializeEntry(entry: MutableMap.MutableEntry<Item, String>):
            Array<String> {
        var serializedKey = serializeClassName(entry.key)
        var quantity = entry.value
        return arrayOf(serializedKey,quantity)


    }
    private fun serializeClassName(itemClass: Item): String{
        return itemClass::class.qualifiedName!!
    }

    fun updateSpotVisit(spot: Spot){
        database.execSQL("UPDATE spots SET visited = 1 " +
                "WHERE index_within_city = ${spot.locationWithinCity}" +
                " and city_x = ${spot.cityX} and city_y = ${spot.cityY}")
    }
    fun updateSpotItems(spot: Spot){
        var serializedItemsMap = serializeItemMap(spot.itemsInside)
        database.execSQL("UPDATE spots SET inner_items_map = '${serializedItemsMap}' " +
                "WHERE index_within_city = ${spot.locationWithinCity}" +
                " and city_x = ${spot.cityX} and city_y = ${spot.cityY}")
    }

    fun setThirst(value: Int){
        database.execSQL("UPDATE stats SET quantity = '$value' WHERE stat = 'thirst'")
    }

    fun setHunger(value: Int){
        database.execSQL("UPDATE stats SET quantity = '$value' WHERE stat = 'hunger'")
    }
    fun setInventory(itemsMap: LinkedHashMap<Item,String>){
        var serializedInventory = serializeItemMap(itemsMap)
        database.execSQL("UPDATE stats SET quantity = '$serializedInventory' WHERE stat = 'inventory'")
    }
    fun getInventory(): LinkedHashMap<Item,String>{
        var inventoryQuery: Cursor = database.rawQuery(getInventoryQuery(),null)
        return extractInventory(inventoryQuery)
    }
    private fun getInventoryQuery(): String{
        return "SELECT quantity FROM stats WHERE stat = 'inventory'"
    }

    private fun extractInventory(results: Cursor):  LinkedHashMap<Item,String>{
        results.moveToFirst()
        return unserializeItemsMap(results)
    }
    private fun unserializeInventory(itemMapText: String): LinkedHashMap<Item,String>{
        var gson = Gson()
        return gson.fromJson(itemMapText, LinkedHashMap<Item,String>().javaClass)
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
        locationList.add(query.getString(query.getColumnIndex("quantity")).toInt())
        query.moveToNext()
        locationList.add(query.getString(query.getColumnIndex("quantity")).toInt())
        return locationList
    }

    fun updatePlayerLocation(x: Int, y: Int){
        database.execSQL("UPDATE stats SET quantity = '$x' WHERE stat = 'x_position'")
        database.execSQL("UPDATE stats SET quantity = '$y' WHERE stat = 'y_position'")
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