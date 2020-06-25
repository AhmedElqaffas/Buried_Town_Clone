package com.example.buriedtownclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.common.collect.LinkedHashMultimap
import kotlinx.android.synthetic.main.new_game.*

class MainActivity : AppCompatActivity() {

    var database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_game)

        initializeDatabase()
    }

    private fun initializeDatabase(){
        Database.context = this
        database.initializeDatabase()
    }

    override fun onResume() {
        super.onResume()
        showOrHideContinueGameButton()
    }

    /* Shows the "Continue Game" button if there are items in stats table (checks the hp as a sample)
       Otherwise, keep it hidden
    */
    private fun showOrHideContinueGameButton(){
        if(database.getHealthPoints() != null){
            continueGameButton.visibility = View.VISIBLE
        }
    }

    fun continueGame(view: View){
        goToGame(false)
    }

    fun newGame(view: View){
        goToGame(true)
    }
    private fun goToGame(newGame: Boolean){
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("new game",newGame)
        startActivity(intent)
    }

    fun goToTutorial(view: View){

    }

    fun goToSettings(view: View){

    }

    fun goToCredits(view: View){

    }

}