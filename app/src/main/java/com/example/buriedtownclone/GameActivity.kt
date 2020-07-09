package com.example.buriedtownclone

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception
import kotlin.math.floor

class GameActivity : AppCompatActivity() {

    private var citiesVisitedList: MutableList<City> = mutableListOf()
    private lateinit var currentCity: City
    private var handler= Handler()
    private var mediaPlayer = MediaPlayer()
    private var isNavigating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initializeClassVariables()
        initializeOrLoadGameData()
        setupViewPagersListeners()
        startCountingTime()
    }

    override fun onBackPressed() {

        TimeHandler.stopTimer()
        GameHandler.freeData()
        super.onBackPressed()
    }


    override fun onResume() {
        super.onResume()
        refreshClassVariables()
        showStatsBarFragment()
    }

    private fun initializeOrLoadGameData(){
        if(isNewGame()){
            initializeGameData()
            updatePlayerData()
            setupViewPagersAdapters()
            goToCity(Definitions.homeX, Definitions.homeY)
            VisualsUpdater.showWelcomingDialog(rootLayout, horizontalGuideline_lastPart)
        }
        else{
            loadGameData()
            setupViewPagersAdapters()
            goToCity(Player.getLocationX(),Player.getLocationY())
        }
    }
    private fun isNewGame(): Boolean{
        val intent: Intent = intent
        return intent.getBooleanExtra("new game", true)
    }

    private fun initializeClassVariables(){
        Database.context = this
        TimeHandler.context = this
        VisualsUpdater.activity = this
        GameHandler.context = this
        VisualsUpdater.isDialogActive = false
    }

    private fun initializeGameData(){
        Database.deleteAllData()
        Database.initializeStats()
        Database.initializeTimestamps()
    }

    private fun refreshClassVariables(){
        Database.context = this
        GameHandler.context = this
        VisualsUpdater.activity = this
        TimeHandler.context = this
    }

    private fun loadGameData(){
        loadPlayerStats()
        loadVisitedCities()
        loadHomeEquipment()
    }

    private fun loadPlayerStats(){
        Player.updateStatsFromDatabase()
    }

    private fun updatePlayerData(){
        Player.updateStatsFromDatabase()
    }

    private fun loadHomeEquipment(){
        HomeSpot.updateEquipmentFromDatabase()
    }

    private fun loadVisitedCities(){
        citiesVisitedList = loadVisitedCitiesFromDatabase()
    }

    private fun loadVisitedCitiesFromDatabase(): MutableList<City>{
        return Database.getCities()
    }

    private fun showStatsBarFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.statsBarContainer, StatsBarFragment(),"stats bar")
            .commit()
    }

    private fun setupViewPagersAdapters(){
        horizontalViewPager.adapter = HorizontalFragmentsAdapter(supportFragmentManager, lifecycle)
        verticalViewPager.adapter = VerticalFragmentsAdapter(supportFragmentManager, lifecycle)
    }
    private fun setupViewPagersListeners(){
        horizontalViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                navigate()
            }
        })
        verticalViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                 super.onPageSelected(position)
                navigate()
             }
        })
    }

    private fun navigate(){
        disableUserInteraction()
        VisualsUpdater.showWalkingPanel()
        playSound()
        waitForSomeTimeToSimulateWalking()
        isNavigating = true
        TimeHandler.startNavigationStatsDecrease()

    }
    private fun waitForSomeTimeToSimulateWalking(){

            handler.postDelayed({
                endWalkingEffect()
            },1500)

    }
    private fun endWalkingEffect(){
        TimeHandler.stopNavigationStatsDecrease()
        enableUserInteraction()
        stopSound()
        updatePlayerLocation()
        if(GameHandler.isGameFinished) {
            return
        }
        loadOrCreateCity()
        VisualsUpdater.hideWalkingPanel()
        isNavigating = false
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
        currentCity =  getVisitedCityObject(locationX,locationY)
        loadAndShowCityFragment(currentCity)

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
        currentCity =  City(locationX, locationY)
        createAndShowCityFragment()
        saveCity(currentCity)
    }

    private fun createAndShowCityFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.gameMapContainer, CityFragment(currentCity,true)).commit()
    }
    private fun saveCity(city: City){
        saveCityInDatabase(city.locationX, city.locationY)
        saveCityObject(city)
    }
    private fun saveCityInDatabase(locationX: Int, locationY: Int){
        Database.saveCity(locationX,locationY)
    }
    private fun saveCityObject(city: City){
        citiesVisitedList.add(city)
    }

    private fun goToCity(x: Int, y: Int){
        horizontalViewPager.setCurrentItem(gameLocationToHorizontalPagerPosition(x),false)
        verticalViewPager.setCurrentItem(gameLocationToVerticalPagerPosition(y),false)
        loadOrCreateCity()
    }

    private fun updatePlayerLocation(){
        Player.setLocation(getHorizontalGameLocation(),getVerticalGameLocation())
    }
    // ViewPagers position are from 0:size, but we want our game to be -0.5 size : 0.5 size
    // and home would be in the middle (x-0, y=0)
    private fun getHorizontalGameLocation(): Int{
        return horizontalViewPager.currentItem - floor(GameSettings.horizontalSize/2.0).toInt()
    }
    private fun getVerticalGameLocation(): Int{
        return verticalViewPager.currentItem - floor(GameSettings.verticalSize/2.0).toInt()
    }

    // To convert the other way around
    private fun gameLocationToHorizontalPagerPosition(locationX: Int): Int{
        return locationX + floor(GameSettings.horizontalSize/2.0).toInt()
    }
    private fun gameLocationToVerticalPagerPosition(locationY: Int): Int{
        return locationY + floor(GameSettings.verticalSize/2.0).toInt()
    }

    private fun startCountingTime(){
        TimeHandler.startTimer()
    }

    private fun disableUserInteraction(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun enableUserInteraction(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun playSound(){
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+this.packageName+"/raw/run") )
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.prepareAsync()

    }
    private fun stopSound(){
        mediaPlayer.release()
    }


}