package com.example.buriedtownclone

import android.content.Intent
import android.content.res.Resources
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception
import java.lang.Math.ceil
import java.net.URI
import kotlin.math.floor

class GameActivity : AppCompatActivity() {

    lateinit var database: Database
    lateinit var player: Player
    var citiesVisitedList: MutableList<City> = mutableListOf()
    lateinit var timeHandler: TimeHandler
    var visualsUpdater = VisualsUpdater(this)
    var gameHandler = GameHandler(this)
    var handler= Handler()
    var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        initializeClassVariables()
        initializeOrLoadGameData()
        showPlayerStatsInStatsBar()
        setupViewPagersAdapters()
        goToHomeCity()
        setupViewPagersListeners()
        startCountingTime()
    }

    override fun onStop() {
        super.onStop()
        timeHandler.stopTimer()
    }
    override fun onRestart() {
        super.onRestart()
        timeHandler.startTimer()
    }

    private fun initializeOrLoadGameData(){
        if(isNewGame()){
            initializeGameData()
            updatePlayerData()
        }
        else{
            loadGameData()
        }
    }
    private fun isNewGame(): Boolean{
        val intent: Intent = intent
        return intent.getBooleanExtra("new game", true)
    }

    private fun initializeGameData(){
        database.deleteAllData()
        database.initializeStats()
    }

    private fun initializeClassVariables(){
        database = Database(this)
        player = Player(this)
        timeHandler = TimeHandler(this,player)
    }

    private fun loadGameData(){
        loadPlayerStats()
        loadVisitedCities()
    }

    private fun loadPlayerStats(){
        val hp = database.getHealthPoints()!!
        val hunger = database.getHunger()
        val thirst = database.getThirst()
        visualsUpdater.updateHealthPoints(hp)
        visualsUpdater.updateHunger(hunger)
        visualsUpdater.updateThirst(thirst)
        player.setHealthPoints(hp)
        player.setHunger(hunger)
        player.setThirst(thirst)
    }

    private fun updatePlayerData(){
        player.updateStatsFromDatabase()
    }

    private fun loadVisitedCities(){
        citiesVisitedList = loadVisitedCitiesFromDatabase()
    }

    private fun loadVisitedCitiesFromDatabase(): MutableList<City>{
        return database.getCities()
    }

    private fun showPlayerStatsInStatsBar(){
        var statsArray = player.getPlayerStats()
        hpTextView.text = statsArray[0].toString()
        hungerTextView.text = statsArray[1].toString()
        thirstTextView.text = statsArray[2].toString()
    }

    private fun setupViewPagersAdapters(){
        horizontalViewPager.adapter = HorizontalFragmentsAdapter(supportFragmentManager, lifecycle);
        verticalViewPager.adapter = VerticalFragmentsAdapter(supportFragmentManager, lifecycle);
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
        playSound(R.raw.run)
        Handler(Looper.getMainLooper()).postDelayed(object: Runnable{
            override fun run() {
                loadOrCreateCity()
                enableUserInteraction()
                stopSound()
            }
        },1500)

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
        horizontalViewPager.setCurrentItem(floor(GameSettings.horizontalSize / 2.0).toInt(),false)
        verticalViewPager.setCurrentItem(floor(GameSettings.horizontalSize / 2.0).toInt(),false)
        loadOrCreateCity()
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

    private fun disableUserInteraction(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private fun enableUserInteraction(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private fun playSound(resource: Int){
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+this.packageName+"/raw/run") )
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
        }
        mediaPlayer.prepareAsync()

    }
    private fun stopSound(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }


}