package com.app.okra.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.preference.PreferenceManager
import com.app.okra.extension.navigationOnly
import com.app.okra.utils.AppConstants
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashBoardActivity : BaseActivity() {

    private var navHost: NavHostFragment? =null
    private var navController: NavController? =null

    override fun getViewModel(): BaseViewModel? {
     return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initialize()

       val email= PreferenceManager.getString(AppConstants.Pref_Key.EMAIL_ID)

      val name=   PreferenceManager.getString(AppConstants.Pref_Key.NAME)

        val textToSet = if(!name.isNullOrEmpty()){
            " You logged in as '${name}'\nYour email is '${email}'"
        }else{
            "Your email is: ${email}"
        }
        tvDetails.text = textToSet
        println("::: onCreate - B" )
    }

    override fun onStart() {
        super.onStart()
        println("::: onStart - B" )
    }
    override fun onRestart() {
        super.onRestart()
        println("::: onRestart - B" )
    }

    override fun onResume() {
        super.onResume()
        println("::: onResume - B" )
    }

    override fun onPause() {
        super.onPause()
        println("::: onPause - B" )
    }

    override fun onStop() {
        super.onStop()
        println("::: onStop - B" )
    }

    override fun onDestroy() {
        super.onDestroy()
        println("::: onDestroy - B" )
    }



    private fun initialize() {
       /* navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment?
        navController = navHost?.navController*/

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment: Fragment =  navHost?.childFragmentManager?.fragments?.get(0)!!
        fragment.onActivityResult(requestCode, resultCode, data)

    }

    fun onAccountClick(view: View) {
        navigationOnly(MyAccountLocalActivity())

    }
    fun onLogClick(view: View) {
        navigationOnly(LogLocalActivity())

    }

}
