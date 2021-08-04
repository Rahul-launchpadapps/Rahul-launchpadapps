package com.app.okra.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.okra.R
import com.app.okra.ui.boarding.login.LoginActivity
import com.app.okra.ui.boarding.resetPassword.ResetOrChangePasswordActivity
import com.app.okra.ui.profile.ProfileFragment
import com.app.okra.utils.AppConstants
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    var fromScreen: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        fromScreen = intent.getStringExtra(AppConstants.SCREEN_TYPE)
        setView()
    }

    private fun setView() {
        if(fromScreen == ProfileFragment::class.java.simpleName){
            btnSend.text = getString(R.string.ok)
        }
    }

    fun onSendClick(view: View) {
        if(fromScreen == ResetOrChangePasswordActivity::class.java.simpleName){
            finishAffinity();
            startActivity(
                    Intent(this, LoginActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }else{
            finish()
        }
    }
}