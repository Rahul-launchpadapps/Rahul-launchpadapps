package com.app.okra.ui.tutorial

import android.content.Intent
import android.os.Bundle
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel

import android.text.Spannable

import android.text.style.ImageSpan
import android.graphics.BitmapFactory

import android.text.SpannableStringBuilder
import com.app.okra.extension.navigate
import com.app.okra.extension.navigationOnly
import com.app.okra.ui.boarding.resetPassword.ResetOrChangePasswordActivity
import com.app.okra.ui.profile.ProfileFragment
import com.app.okra.utils.AppConstants

import kotlinx.android.synthetic.main.activity_app_tutorial.*
import kotlinx.android.synthetic.main.layout_header.*


class AppTutorialActivity : BaseActivity() {

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_tutorial)
        setImage()
    }

    private fun setImage() {
        val ssb = SpannableStringBuilder("   ")
        val smiley = BitmapFactory.decodeResource(resources, R.mipmap.okra_home_logo)
        ssb.setSpan(ImageSpan(smiley), 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        tvMessage2.text = getString(R.string.app_tutorial_message1)
        tvMessage2.append(ssb)
        tvMessage2.append(getString(R.string.app_tutorial_message2))

        ivPlay.setOnClickListener {
            navigate(
                Intent(this, VideoPlayerActivity::class.java)
                    .putExtra(AppConstants.DATA, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
            )
        }

        ivBack.setOnClickListener {
            finish()
        }
    }
}