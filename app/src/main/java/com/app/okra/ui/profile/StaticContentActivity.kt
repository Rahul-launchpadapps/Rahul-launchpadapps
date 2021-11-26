package com.app.okra.ui.profile

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.preference.PreferenceManager
import com.app.okra.data.repo.ProfileRepoImpl
import com.app.okra.extension.beInvisible
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.ItemModel
import com.app.okra.ui.profile.ItemsAdapter
import com.app.okra.utils.AppConstants
import com.app.okra.utils.AppConstants.Intent_Constant.Companion.TYPE
import com.app.okra.utils.Listeners
import com.app.okra.utils.MessageConstants
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_static_content.*
import kotlinx.android.synthetic.main.layout_button.*
import kotlinx.android.synthetic.main.layout_button.view.*
import kotlinx.android.synthetic.main.layout_header.*


class StaticContentActivity : BaseActivity(),
        View.OnClickListener{


    private var type: String? = null

    companion object{
        const val TERMS_AND_CONDITION = "Terms & conditions"
        const val PRIVACY_POLICY = "Privacy Policy"
        const val ABOUT_US = "About Us"
        const val FAQ = "FAQ"
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static_content)
        getIntentData()
        setViews()
        initView()
        setListener()
        loadData()
    }

    private fun loadData() {
        val urlToLoad = when(type){
            TERMS_AND_CONDITION ->  AppConstants.ContentManagementUrl.TERM_AND_COND_URL
            ABOUT_US -> AppConstants.ContentManagementUrl.ABOUT_US_URL
            PRIVACY_POLICY ->  AppConstants.ContentManagementUrl.PRIVACY_POLICY_URL
            else -> AppConstants.ContentManagementUrl.FAQ_URL
        }
        loadPage(urlToLoad)
    }

    private fun getIntentData() {
        type = intent.getStringExtra(TYPE)
    }

    private fun initView() {
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
    }

    private fun loadPage(url: String) {
        if(isInternetAvailable()) {
            showProgressBar()
            webView.loadUrl(url)
            webView.isVerticalScrollBarEnabled = true;
            webView.isHorizontalScrollBarEnabled = true;

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    hideProgressBar()
                }
            }
        }else{
            showToast(MessageConstants.Messages.internet_connection_required)
        }
    }

    private fun setListener() {
        ivBack.setOnClickListener(this)
    }


    private fun setViews() {
        ivDelete.beInvisible()
        tvTitle.text =
                when(type){
                    TERMS_AND_CONDITION ->  getString(R.string.title_terms_and_condition)
                    ABOUT_US ->  getString(R.string.title_about_us)
                    PRIVACY_POLICY ->  getString(R.string.title_privacy_policy)
                    else ->  getString(R.string.title_faq)
                }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                finish()
            }
        }
    }



}