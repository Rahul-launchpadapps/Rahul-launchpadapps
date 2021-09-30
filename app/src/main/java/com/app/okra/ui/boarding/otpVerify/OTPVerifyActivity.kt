package com.app.okra.ui.boarding.otpVerify


import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.app.okra.data.repo.OTPVerifyRepoImpl
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.network.ApiData
import com.app.okra.extension.navigate
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.InitialBoardingResponse
import com.app.okra.ui.boarding.resetPassword.ResetOrChangePasswordActivity
import com.app.okra.utils.AppConstants
import com.app.okra.utils.MessageConstants
import kotlinx.android.synthetic.main.activity_otp_verify.*

class OTPVerifyActivity : BaseActivity() {

    private lateinit var data: InitialBoardingResponse
    private val viewModel: OTPVerifyViewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                OTPVerifyViewModel(OTPVerifyRepoImpl(apiService))
            }
        ).get(OTPVerifyViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    private var email: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)
        makeStatusBarTransparent()
        getIntentData()
        manageView()
        setObserver()
        setListener()
    }

    private fun getIntentData() {
        if(intent.hasExtra(AppConstants.Intent_Constant.EMAIL))
            email = intent.getStringExtra(AppConstants.Intent_Constant.EMAIL).toString()

    }

    private fun manageView() {
        val subText = "${getString(R.string.we_have_sent_a)} $email.\n${getString(R.string.verify_your_otp)} "
        tvSubHeader.text = subText


        val span = SpannableString(tvResendCode.text)
        span.setSpan(UnderlineSpan(), 0, tvResendCode.text.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        tvResendCode.text = span
        tvResendCode.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun setObserver() {
        viewModel._OtpVerifyLiveData.observe(this) { it ->
            navigate(
                Intent(this, ResetOrChangePasswordActivity::class.java)
                    .putExtra(AppConstants.EMAIL, email)
                        .putExtra(AppConstants.SCREEN_TYPE, OTPVerifyActivity::class.java.simpleName)

            )

        }

        viewModel._OtpResendLiveData.observe(this) {
            val apiSuccess: ApiData<*> = it as ApiData<*>

            if (apiSuccess.statusCode == "200") {
                showToast(MessageConstants.Messages.we_have_sent)
            } else {
                apiSuccess.message?.let { showToast(it) }
            }
        }
        viewModel._errorObserver.observe(this) { it ->
            it.getContent()?.let {
                showToast(it.message!!)
                if (it.statusCode == "400"){
                    tvInvalidCode.visibility = View.VISIBLE
                    otp_view.setItemBackground(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.otp_error,
                            null
                        )
                    )
                } else {
                    otp_view.setItemBackground(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.otp,
                            null
                        )
                    )

                    tvInvalidCode.visibility = View.GONE
                }
            }

        }

        viewModel._toastObserver.observe(this) { it ->
            it.getContent()?.let {
                showToast(it.message)
                if (it.message == MessageConstants.Errors.invalid_otp) {
                    tvInvalidCode.visibility = View.VISIBLE
                    otp_view.setItemBackground(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.otp_error,
                            null
                        )
                    )
                } else {
                    otp_view.setItemBackground(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.mipmap.otp,
                            null
                        )
                    )

                    tvInvalidCode.visibility = View.GONE
                }
            }
    }

        viewModel._progressDialog.observe(this) {
            showHideProgress(it.getContent()!!.status);
        }
    }

    private fun setListener() {
        //tv_resend_code.setOnClickListener(this)
    }


    fun onOtpVerifyClick(view: View) {
        /* viewModel.setVerifyOtpValue(phoneNumber!!, email!!, otp_view.text.toString(), screenType!!)
         viewModel.otpVerifyApi()*/

    }

    fun onOTPVerifyBackClick(view: View) {
        finish()
    }



    fun onBackClick(view: View) {
        finish()
    }
    fun onVerifyClick(view: View) {
        viewModel.setVerifyOtpValue(email, otp_view.text.toString())
        viewModel.otpVerifyApi()
    }

    fun onResendClick(view: View) {
        viewModel.setResetOTPValue( email, "EMAIL")
        viewModel.otpReSendApi()
    }
}
