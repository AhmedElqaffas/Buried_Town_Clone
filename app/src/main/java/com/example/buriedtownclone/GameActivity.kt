package com.example.buriedtownclone

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception
import java.lang.Math.ceil
import kotlin.math.floor

class GameActivity : AppCompatActivity() {

    lateinit var database: Database
    lateinit var player: Player
    var citiesVisitedList: MutableList<City> = mutableListOf()
    lateinit var timeHandler: TimeHandler
    var visualsUpdater = VisualsUpdater(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initializeGameData()
        showPlayerStatsInStatsBar()
        setupViewPager()
        goToHomeCity()
        startCountingTime()
    }

    private fun initializeGameData(){
        database = Database(this)
        database.deleteAllData()
        database.initializeStats()
        player = Player(this)
        timeHandler = TimeHandler(this,player)

    }

    private fun showPlayerStatsInStatsBar(){
        var statsArray = player.getPlayerStats()
        hpTextView.text = statsArray[0].toString()
        hungerTextView.text = statsArray[1].toString()
        thirstTextView.text = statsArray[2].toString()
    }

    private fun setupViewPager(){
        horizontalViewPager.adapter = HorizontalFragmentsAdapter(supportFragmentManager, lifecycle);
        verticalViewPager.adapter = VerticalFragmentsAdapter(supportFragmentManager, lifecycle);
        horizontalViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                loadOrCreateCity()
            }
        })
        verticalViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                loadOrCreateCity()
            }
        })
    }

    private fun loadOrCreateCity(){
        if(isCityAlreadyVisited(getHorizontalGameLocation(), getVerticalGameLocation())){
            loadCity(getHorizontalGameLocation(), getVerticalGameLocation())
        }else {
            createAndSaveCity(getHorizontalGameLocation(), getVerticalGameLocation())
        }
    }
    private fun isCityAlreadyVisited(locationX: Int, locationY: Int): Boolean{
        for(city in citiesVisitedList){
            if(locationX == city.locationX && locationY == city.locationY){
                return true
            }
        }
        return false
    }
    private fun loadCity(locationX: Int, locationY: Int){
        var city = getVisitedCityObject(locationX,locationY)
        loadAndShowCityFragment(city)
    }
    private fun getVisitedCityObject(locationX: Int, locationY: Int): City{
        for(city in citiesVisitedList){
            if(city.locationX == locationX && city.locationY == locationY){
                return city
            }
        }
        throw Exception("City visited but not found")
    }
    private fun loadAndShowCityFragment(city: City){
        supportFragmentManager.beginTransaction().replace(R.id.gameMapContainer, CityFragment(city,false)).commit()
    }

    private fun createAndSaveCity(locationX: Int, locationY: Int ){
        var city = City(locationX, locationY)
        createAndShowCityFragment(city)
        saveCity(city)
    }

    private fun createAndShowCityFragment(city: City){
        supportFragmentManager.beginTransaction().replace(R.id.gameMapContainer, CityFragment(city,true)).commit()
    }
    private fun saveCity(city: City){
        saveCityInDatabase(city.locationX, city.locationY)
        saveCityObject(city)
    }
    private fun saveCityInDatabase(locationX: Int, locationY: Int){
        database.saveCity(locationX,locationY)
    }
    private fun saveCityObject(city: City){
        citiesVisitedList.add(city)
    }

    private fun goToHomeCity(){
        horizontalViewPager.setCurrentItem(kotlin.math.floor(GameSettings.horizontalSize / 2.0).toInt(),false)
        verticalViewPager.setCurrentItem(kotlin.math.floor(GameSettings.horizontalSize / 2.0).toInt(),false)
    }

    // ViewPagers position are from 0:size, but we want our game to be -0.5 size : 0.5 size
    // and home would be in the middle (x-0, y=0)
    private fun getHorizontalGameLocation(): Int{
        return horizontalViewPager.currentItem - floor(GameSettings.horizontalSize/2.0).toInt()
    }
    private fun getVerticalGameLocation(): Int{
        return verticalViewPager.currentItem - floor(GameSettings.verticalSize/2.0).toInt()
    }

    private fun startCountingTime(){
        timeHandler.startTimer();
    }
}