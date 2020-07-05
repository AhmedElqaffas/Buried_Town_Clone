package com.example.buriedtownclone


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

open class DialogFragment : Fragment() {

    private lateinit var inflated: ConstraintLayout
    private lateinit var parentContainerLayout: ConstraintLayout
    private var listOfConversationDialogs: MutableList<MutableList<Any>> = mutableListOf()
    private var communicationInterface : CommunicationInterface? = null


    override fun onPause() {
        endDialog()
        super.onPause()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflated =  inflater.inflate(R.layout.fragment_dialog, container, false) as ConstraintLayout
        val dialogTextView = inflated.findViewById<TextView>(R.id.dialog)
        dialogTextView.setOnClickListener {dialogClicked(dialogTextView)}
        return inflated
    }

    override fun onResume() {
        super.onResume()
        postThatFragmentIsReady()
    }

    interface CommunicationInterface{
        fun onFragmentReady(){

        }
    }

    fun setInterfaceListener(listener: CommunicationInterface){
        this.communicationInterface = listener
    }

    private fun postThatFragmentIsReady(){
        communicationInterface?.onFragmentReady()
    }

    fun createWelcomingDialog(parentContainerLayout: ConstraintLayout){
        this.parentContainerLayout = parentContainerLayout
        listOfConversationDialogs.clear()
        listOfConversationDialogs.add(mutableListOf(R.drawable.player_avatar,"An abandoned town ... at least I am away from Spongebob"))
        listOfConversationDialogs.add(mutableListOf(R.drawable.spongebob_laugh, "*A loud annoying laugh echoes* \n BA-HH-HH-HA-HA-HA"))
        listOfConversationDialogs.add(mutableListOf(R.drawable.player_avatar, "Oh, barnacles!"))
        goToNextDialog()
    }

    fun createFirstHomeVisitDialog(parentContainerLayout: ConstraintLayout){
        this.parentContainerLayout = parentContainerLayout
        listOfConversationDialogs.clear()
        listOfConversationDialogs.add(mutableListOf(R.drawable.player_avatar,"Hmmmm, looks like a suitable place to live in"))
        listOfConversationDialogs.add(mutableListOf(R.drawable.player_avatar, "There is something down there"))
        listOfConversationDialogs.add(mutableListOf(R.drawable.pistol_avatar, "You've found a pistol"))
        listOfConversationDialogs.add(mutableListOf(R.drawable.player_avatar, "This should be useful"))
        goToNextDialog()
    }

    private fun dialogClicked(view: View){
        try{
            goToNextDialog()
        }catch (ex: Exception){
            endDialog()
        }
    }

    private fun goToNextDialog(){
        changeDialogImage()
        changeDialogText()
        listOfConversationDialogs.removeAt(0)
    }
    
    private fun changeDialogImage(){
        val avatar = inflated.findViewById<ImageView>(R.id.avatar)
        avatar.setBackgroundResource(listOfConversationDialogs[0][0] as Int)
    }
    
    private fun changeDialogText(){
        val dialog = inflated.findViewById<TextView>(R.id.dialog)
        dialog.text = listOfConversationDialogs[0][1].toString()
    }

    private fun endDialog(){
       VisualsUpdater.hideDialog(parentContainerLayout)
    }
}