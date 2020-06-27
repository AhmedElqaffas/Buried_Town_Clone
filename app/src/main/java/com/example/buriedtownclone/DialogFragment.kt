package com.example.buriedtownclone

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_dialog.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DialogFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var inflated: ConstraintLayout
    private lateinit var parentContainerLayout: ConstraintLayout
    private var listOfConversationDialogs: MutableList<MutableList<Any>> = mutableListOf()
    private var communicationInterface : CommunicationInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflated =  inflater.inflate(R.layout.fragment_dialog, container, false) as ConstraintLayout
        val dialogTextView = inflated.findViewById<TextView>(R.id.dialog)
        dialogTextView.setOnClickListener {dialogClicked(dialogTextView)}
        postThatFragmentIsReady()
        return inflated
    }

    interface CommunicationInterface{
        fun onFragmentReady(){

        }
    }

    fun setInterfaceListener(listener: CommunicationInterface){
        this.communicationInterface = listener
    }

    private fun postThatFragmentIsReady(){
        communicationInterface!!.onFragmentReady()
    }

    fun createWelcomingDialog(parentContainerLayout: ConstraintLayout){
        this.parentContainerLayout = parentContainerLayout
        listOfConversationDialogs.clear()
        listOfConversationDialogs.add(mutableListOf(R.drawable.player_avatar,"An abandoned town ... at least I am away from Spongebob"))
        listOfConversationDialogs.add(mutableListOf(R.drawable.spongebob_laugh, "*A loud annoying laugh echoes* \n BA-HH-HH-HA-HA-HA"))
        listOfConversationDialogs.add(mutableListOf(R.drawable.player_avatar, "Oh, barnacles!"))
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
       val visualsUpdater = VisualsUpdater()
        visualsUpdater.hideDialog(parentContainerLayout)
    }
}