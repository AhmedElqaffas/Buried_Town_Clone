package com.example.buriedtownclone

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.common.collect.LinkedListMultimap
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"


class CityFragment(private val city: City, private val isNewCity: Boolean): Fragment(){

    private val buildingsProbabilities: HashMap<Int, Double> = hashMapOf(R.drawable.house to 0.4,
        R.drawable.workshop to 0.2, R.drawable.school to 0.2, R.drawable.pharmacy to 0.1,
        R.drawable.police_station to 0.1)

    private val stringToSpotImage: HashMap<String, Int> = hashMapOf(Definitions.house to R.drawable.house,
    Definitions.school to R.drawable.school, Definitions.pharmacy to R.drawable.pharmacy,
    Definitions.policeStation to R.drawable.police_station, Definitions.workshop to R.drawable.workshop,
    Definitions.home to R.drawable.home)

    private val spotImageToString: HashMap<Int, String> = hashMapOf(R.drawable.house to Definitions.house,
        R.drawable.school to Definitions.school , R.drawable.pharmacy to Definitions.pharmacy ,
        R.drawable.police_station to Definitions.policeStation , R.drawable.workshop to Definitions.workshop,
        R.drawable.home to Definitions.home)

    var database = Database()

    /*// TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
*/
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

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
            else -> inflater.inflate(R.layout.fragment_home_city, container, false)
        }
    }

    private fun createCity(inflated: View){
        val chosenBuildingsImages: MutableList<Int> = randomizeSpotsBuildings()
        setImageViewsImagesAndListeners(chosenBuildingsImages, inflated as ConstraintLayout)
        val citySpotsList = createCitySpots(chosenBuildingsImages)
        saveSpots(citySpotsList)
    }
    private fun randomizeSpotsBuildings(): MutableList<Int>{
        val chosenBuildingsList: MutableList<Int> = mutableListOf()
        var buildingImageId: Int
        for (i in 0 until city.numberOfSpotsWithin) {
            buildingImageId = if(isHomeSpot(i)){
                R.drawable.home
            } else {
                getRandomBuildingBasedOnProbability()
            }
                chosenBuildingsList.add(buildingImageId)
        }
        return chosenBuildingsList
    }
    private fun isHomeSpot(spotIndex: Int): Boolean{
        return (city.numberOfSpotsWithin == 6 && spotIndex == 2)
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
        throw RuntimeException("Should never be shown.")
    }

    private fun setImageViewsImagesAndListeners(buildingsImagesId: MutableList<Int>,
                                                layout: ConstraintLayout){
        var imagesFound = 0
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
        val spotObjectsList = mutableListOf<Spot>()
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
        val spot = if(spotType == "home"){
                        HomeSpot()
                    } else{
                        NormalSpot()
                    }
        spot.cityX = city.locationX
        spot.cityY = city.locationY
        spot.locationWithinCity = indexWithinCity
        spot.visited = false
        spot.spotType = spotType
        spot.itemsInside = if(spotType == Definitions.home) LinkedListMultimap.create() else generateItemsInsideSpot()
        return spot
    }
    private fun generateItemsInsideSpot(): LinkedListMultimap<Item,String>{
        val itemsMap: LinkedListMultimap<Item,String> = LinkedListMultimap.create()
        itemsMap.put(Tuna(),"10")
        itemsMap.put(Apple(),"1")
        itemsMap.put(Water(),"2")
        itemsMap.put(mm9(),"7")
        itemsMap.put(Wood(), "2")
        itemsMap.put(Nail(), "3")
        return itemsMap
    }
    private fun saveSpots(citySpotsList: MutableList<Spot>){
        for(spot in citySpotsList){
            city.spots.add(spot)
            spot.saveInDatabase()
        }
    }

    private fun loadCity(city: City, inflated: View){
        val spotsList: MutableList<Spot> = getSpotsInCity(city)
        loadSpots(spotsList, inflated as ConstraintLayout)
    }
    private fun getSpotsInCity(city: City): MutableList<Spot>{
        return city.spots
    }

    private fun loadSpots(spotsList: MutableList<Spot>, layout: ConstraintLayout){
        var spotsDrawn = 0
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
        if(!VisualsUpdater.isDialogActive)
            goToActivity(getSpotIndex(view))

    }
    private fun getSpotIndex(view: View): Int{
        val imageIndexWithinParent = (view.parent as ViewGroup).indexOfChild(view)
        return imageIndexWithinParent - 4 // The first ImageView starts at index 4 at parent
    }

    private fun goToActivity(index: Int){
        val activity = city.spots[index].getActivityToOpen()
        val requestCode = city.spots[index].getActivityRequestCode()
        val intent = Intent(context, activity)
        intent.putExtra(Definitions.spotItems,city.spots[index])
        this.startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateValuesOrThrowException(requestCode,resultCode,data)
    }
    private fun updateValuesOrThrowException(requestCode: Int, resultCode: Int, data: Intent?){
        if(resultCode == RESULT_OK){
            if(requestCode == Definitions.normalSpotRequestCode){
                updateSpotItems(data)
            }
            else if (requestCode == Definitions.homeSpotRequestCode){
                // TODO: Implement returning from home spot
            }
        }
        else{
            throw Exception("cityFragment onActivityResult: Problem updating city")
        }
    }

    /**
     * DESCRIPTION: Since passing object to activity happens by value not by reference
     * We have to take the changes that has happened in the spot activity and update it in the
     * original object
     */
    private fun updateSpotItems(data: Intent?){
        val spotVisited = getVisitedSpotObject(data)
        updateCityObject(spotVisited)
        //updateSpotItemsInDatabase(spotVisited)
    }
    private fun getVisitedSpotObject(data: Intent?): Spot{
        return data!!.getSerializableExtra("spot") as Spot
    }
    private fun updateCityObject(spotVisited: Spot){
        city.refreshSpotItems(spotVisited)
    }
    private fun updateSpotItemsInDatabase(spotVisited: Spot){
        database.updateSpotItems(spotVisited)
    }

}