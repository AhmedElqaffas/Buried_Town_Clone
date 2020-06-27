package com.example.buriedtownclone

import android.R
import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*


class VisualsUpdater{

    private lateinit var statsBarFragment: StatsBarFragment
    private lateinit var dialogFragment: DialogFragment

    companion object{

        var activity: Activity? = null
        var isDialogActive = false
        private val timeHandler = TimeHandler()
        var dialogFragmentContainer: FrameLayout? = null
    }

    fun showStatsInStatsBar(player: Player){

        if(activity == null){ // If the game handler ended the game
            return
        }
        getStatsBarFragment()
        statsBarFragment.updateStats(player)
    }

    private fun getStatsBarFragment(){

        val fragmentActivity: FragmentActivity = activity as FragmentActivity
        statsBarFragment = fragmentActivity.supportFragmentManager.
        findFragmentByTag("stats bar") as StatsBarFragment
    }

    fun showWalkingPanel(){
        activity!!.walkingPanel.visibility = View.VISIBLE
    }
    fun hideWalkingPanel(){
        activity!!.walkingPanel.visibility = View.GONE
    }


    fun showWelcomingDialog(parentContainerLayout: ConstraintLayout, upperConstraint: View){

        if(!timeHandler.isStopped())
            timeHandler.stopTimer()

        isDialogActive = true
        createFragmentContainer()
        customizeContainer(parentContainerLayout, upperConstraint)
        loadDialogFragment()

        var handler: Handler = Handler()
        handler.postDelayed({dialogFragment.createWelcomingDialog(parentContainerLayout)},10)
    }

    private fun createFragmentContainer(){
        dialogFragmentContainer =  FrameLayout(activity!!)
        dialogFragmentContainer!!.id = View.generateViewId()
    }

    private fun customizeContainer(parentContainerLayout: ConstraintLayout, upperConstraint: View ){

        parentContainerLayout!!.addView(dialogFragmentContainer)

        val constraintSet = ConstraintSet()
        constraintSet.clone(parentContainerLayout)
        constraintSet.connect(dialogFragmentContainer!!.id, ConstraintSet.TOP, upperConstraint.id,
            ConstraintSet.TOP, 0)
        constraintSet.connect(dialogFragmentContainer!!.id, ConstraintSet.BOTTOM, parentContainerLayout!!.id, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(parentContainerLayout)

        dialogFragmentContainer!!.layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialogFragmentContainer!!.layoutParams.height = 0
    }

    private fun loadDialogFragment(){
        dialogFragment = DialogFragment()
        val fragmentActivity: FragmentActivity = activity as FragmentActivity
        fragmentActivity.supportFragmentManager.
        beginTransaction().replace(dialogFragmentContainer!!.id, dialogFragment,"dialog")
            .commit()
    }


    fun hideDialog(parentContainerLayout: ConstraintLayout){
        //isDialogActive = false
        parentContainerLayout.removeView(dialogFragmentContainer)
        dialogFragmentContainer = null
        dialogFragmentContainer = null
        if(timeHandler.isStopped())
             timeHandler.startTimer()
    }


}