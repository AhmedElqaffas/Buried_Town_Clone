package com.example.buriedtownclone

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.buriedtownclone.homeequipment.Equipment
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.coroutines.Job


object VisualsUpdater: DialogFragment.CommunicationInterface{

    private lateinit var statsBarFragment: StatsBarFragment
    private var dialogFragment: DialogFragment? = null
    private lateinit var parentContainerLayout: ConstraintLayout
    private var coroutineJob = Job()

    var activity: Activity? = null
    var isDialogActive = false
    var dialogFragmentContainer: FrameLayout? = null

    fun showStatsInStatsBar(){
        if(activity == null){ // If the game handler ended the game
            return
        }
        getStatsBarFragment()
        statsBarFragment.updateStats()
    }

    private fun getStatsBarFragment(){
        val fragmentActivity: FragmentActivity = activity as FragmentActivity
        statsBarFragment = fragmentActivity.supportFragmentManager.
        findFragmentByTag("stats bar") as StatsBarFragment
    }

    fun showWalkingPanel(){
        activity?.walkingPanel?.visibility = View.VISIBLE
    }
    fun hideWalkingPanel(){
        activity?.walkingPanel?.visibility = View.GONE
    }


    fun showWelcomingDialog(parentContainerLayout: ConstraintLayout, upperConstraint: View){
        setupFragment(parentContainerLayout, upperConstraint)
        coroutineJob = Job()
        coroutineJob.invokeOnCompletion { startFragmentWelcomeDialog() }
    }

    fun showFirstHomeVisitDialog(parentContainerLayout: ConstraintLayout, upperConstraint: View){
        setupFragment(parentContainerLayout, upperConstraint)
        coroutineJob = Job()
        coroutineJob.invokeOnCompletion { startFragmentFirstHomeVisitDialog() }
    }

    private fun setupFragment(parentContainerLayout: ConstraintLayout, upperConstraint: View){
        this.parentContainerLayout = parentContainerLayout
        if(!TimeHandler.isStopped())
            TimeHandler.stopTimer()
        isDialogActive = true
        createFragmentContainer()
        customizeContainer(upperConstraint)
        loadDialogFragment()
        dialogFragment?.setInterfaceListener(this)
    }

    private fun createFragmentContainer(){
        dialogFragmentContainer =  FrameLayout(activity!!)
        dialogFragmentContainer?.id = View.generateViewId()
    }

    private fun customizeContainer(upperConstraint: View ){

        parentContainerLayout.addView(dialogFragmentContainer)

        val constraintSet = ConstraintSet()
        constraintSet.clone(parentContainerLayout)
        constraintSet.connect(dialogFragmentContainer!!.id, ConstraintSet.TOP, upperConstraint.id,
            ConstraintSet.TOP, 0)
        constraintSet.connect(dialogFragmentContainer!!.id, ConstraintSet.BOTTOM, parentContainerLayout.id, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(parentContainerLayout)

        dialogFragmentContainer?.layoutParams?.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialogFragmentContainer?.layoutParams?.height = 0
    }

    private fun loadDialogFragment(){
        dialogFragment = DialogFragment()
        val fragmentActivity: FragmentActivity = activity as FragmentActivity
        fragmentActivity.supportFragmentManager.
        beginTransaction().replace(dialogFragmentContainer!!.id, dialogFragment!!,"dialog")
            .commit()
    }

    private fun startFragmentWelcomeDialog(){
        dialogFragment?.createWelcomingDialog(parentContainerLayout)
    }

    private fun startFragmentFirstHomeVisitDialog(){
        dialogFragment?.createFirstHomeVisitDialog(parentContainerLayout)
    }


    fun hideDialog(parentContainerLayout: ConstraintLayout){
        isDialogActive = false
        parentContainerLayout.removeView(dialogFragmentContainer)
        dialogFragmentContainer = null
        dialogFragment = null
        if(TimeHandler.isStopped())
             TimeHandler.startTimer()
    }

    override fun onFragmentReady() {
        coroutineJob.complete() // trigger invokeOnCompletion()
    }

    fun showEquipmentDialog(equipmentObject: Equipment, fragmentManager: FragmentManager?){
        val equipmentAlertDialog = EquipmentDialogFragment(equipmentObject)
        fragmentManager?.let { equipmentAlertDialog.show(it, "equipment dialog") }
    }


}