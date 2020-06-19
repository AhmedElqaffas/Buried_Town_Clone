package com.example.buriedtownclone

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CityFragment(val city: City, val isNewCity: Boolean): Fragment(){

    val buildingsProbabilities: HashMap<Int, Double> = hashMapOf(R.drawable.house to 0.4,
        R.drawable.workshop to 0.2, R.drawable.school to 0.2, R.drawable.pharmacy to 0.1,
        R.drawable.police_station to 0.1)

    val stringToSpotImage: HashMap<String, Int> = hashMapOf(Definitions.house to R.drawable.house,
    Definitions.school to R.drawable.school, Definitions.pharmacy to R.drawable.pharmacy,
    Definitions.policeStation to R.drawable.police_station, Definitions.workshop to R.drawable.workshop)

    val spotImageToString: HashMap<Int, String> = hashMapOf(R.drawable.house to Definitions.house,
        R.drawable.school to Definitions.school , R.drawable.pharmacy to Definitions.pharmacy ,
        R.drawable.police_station to Definitions.policeStation , R.drawable.workshop to Definitions.workshop)

    lateinit var database: Database

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        database = Database(activity!!)
        val inflated: View = inflateLayoutHavingNumberOfSpots(inflater, container)
        if(isNewCity) {
            createCity(inflated)
        }
        else{
            loadCity(city,inflated)
        }
        return inflated
    }

    private fun inflateLayoutHavingNumberOfSpots(inflater: LayoutInflater, container: ViewGroup?): View{
        return when(city.numberOfSpotsWithin){
            3 -> inflater.inflate(R.layout.fragment_three_spots_city, container, false)
            4 -> inflater.inflate(R.layout.fragment_four_spots_city, container, false)
            5 -> inflater.inflate(R.layout.fragment_five_spots_city, container, false)
            else -> inflater.inflate(R.layout.fragment_three_spots_city, container, false)
        }
    }

    private fun createCity(inflated: View){
        var chosenBuildingsImages: MutableList<Int> = randomizeSpotsBuildings()
        setImageViewsSourcesToBuildings(chosenBuildingsImages, inflated as ConstraintLayout)
        var citySpotsList = createCitySpots(chosenBuildingsImages)
        saveSpots(citySpotsList)
    }
    private fun randomizeSpotsBuildings(): MutableList<Int>{
        val chosenBuildingsList: MutableList<Int> = mutableListOf()
        for (i in 0 until city.numberOfSpotsWithin) {
            var buildingImageId = getRandomBuildingBasedOnProbability()
            chosenBuildingsList.add(buildingImageId)
        }
        return chosenBuildingsList
    }
    private fun getRandomBuildingBasedOnProbability(): Int{
        var completeWeight = 0.0
        for (building in buildingsProbabilities){
            completeWeight += building.value
        }
        val r = Math.random() * completeWeight
        var countWeight = 0.0
        for (building in buildingsProbabilities) {
            countWeight += building.value
            if (countWeight >= r)
                return building.key
        }
        throw RuntimeException("Should never be shown.");
    }

    private fun setImageViewsSourcesToBuildings(buildingsImagesId: MutableList<Int>,
                                                layout: ConstraintLayout){
        var imagesFound: Int = 0
        for (i in 0 until layout.childCount) {
            val subView: View = layout.getChildAt(i)
            if (subView is ImageView) {
                subView.setOnClickListener{imageViewsClickListener(subView)}
                subView.setImageResource(buildingsImagesId[imagesFound])
                imagesFound++
            }
        }
    }

    private fun createCitySpots(chosenBuildingsImages: MutableList<Int>): MutableList<Spot>{
        var spotObjectsList = mutableListOf<Spot>()
        for(i in 0 until chosenBuildingsImages.size){
            spotObjectsList.add(createSpotObject(i,
                convertImageResourceToString(chosenBuildingsImages[i])))
        }
        return spotObjectsList
    }
    private fun convertImageResourceToString(resource: Int): String{
        return spotImageToString[resource]!!
    }
    private fun createSpotObject(indexWithinCity: Int, spotType: String): Spot{
        var spot = Spot()
        spot.cityX = city.locationX
        spot.cityY = city.locationY
        spot.locationWithinCity = indexWithinCity
        spot.visited = false
        spot.itemsInside = generateItemsInsideSpot()
        spot.spotType = spotType
        return spot
    }
    private fun generateItemsInsideSpot(): HashMap<String,String>{
        var itemsMap: HashMap<String,String> = hashMapOf()
        itemsMap.put("Tuna","10")
        itemsMap.put("Apples","1")
        itemsMap.put("Water","2")
        itemsMap.put("9mm","7")
        return itemsMap
    }
    private fun saveSpots(citySpotsList: MutableList<Spot>){
        saveSpotsInCityObject(citySpotsList)
        saveSpotsInDatabase(citySpotsList)
    }
    private fun saveSpotsInCityObject(citySpotsList: MutableList<Spot>){
        for(spot in citySpotsList)
            city.spots.add(spot)
    }
    private fun saveSpotsInDatabase(citySpotsList: MutableList<Spot>){
        for (spot in citySpotsList)
            database.saveSpot(spot)
    }

    private fun loadCity(city: City, inflated: View){
        var spotsList: MutableList<Spot> = getSpotsInCity(city)
        loadSpots(spotsList, inflated as ConstraintLayout)
    }
    private fun getSpotsInCity(city: City): MutableList<Spot>{
        return city.spots
    }

    private fun loadSpots(spotsList: MutableList<Spot>, layout: ConstraintLayout){
        var spotsDrawn: Int = 0
        for (i in 0 until layout.childCount) {
            val subView: View = layout.getChildAt(i)
            if (subView is ImageView){
                subView.setOnClickListener {imageViewsClickListener(subView)}
                subView.setImageResource(stringToSpotImage[spotsList[spotsDrawn].spotType]!!)
                spotsDrawn++
            }
        }
    }

    private fun imageViewsClickListener(view: View){
        var imageIndexWithinParent = (view.parent as ViewGroup).indexOfChild(view)
        var imageIndex = imageIndexWithinParent - 4 // The first ImageView starts at index 4 at parent
        goToInventoryActivity(imageIndex)
    }

    private fun goToInventoryActivity(index: Int){
        var intent = Intent(context, InventoryActivity::class.java)
        intent.putExtra("spot",city.spots[index])
        this.startActivity(intent)
    }

}