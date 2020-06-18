package com.example.buriedtownclone

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Database(val context: Context){

    companion object{
       lateinit  var database: SQLiteDatabase
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

    fun getSpotsInCity(city: City): Cursor{
        var spotsQuery: Cursor = database.rawQuery(getSpotsQuery(city), null)
        println(getSpotsQuery(city))
        return spotsQuery

    }
    private fun getSpotsQuery(city: City): String{
        return "SELECT * FROM spots WHERE city_x = ${city.locationX} " +
                "and city_y = ${city.locationY}"
    }

    fun saveSpot(spot: Spot){

    }

    fun setThirst(value: Int){
        database.execSQL("UPDATE stats SET quantity = $value WHERE stat = 'thirst'")
    }

    fun setHunger(value: Int){
        database.execSQL("UPDATE stats SET quantity = $value WHERE stat = 'hunger'")
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