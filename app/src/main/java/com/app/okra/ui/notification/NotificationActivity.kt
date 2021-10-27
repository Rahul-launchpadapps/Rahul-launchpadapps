package com.app.okra.ui.notification

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.preference.PreferenceManager
import com.app.okra.data.repo.SettingRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.ui.my_account.setting.SettingsViewModel
import com.app.okra.utils.AppConstants
import kotlinx.android.synthetic.main.layout_header.*


class NotificationActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            SettingsViewModel(SettingRepoImpl(apiServiceAuth))
        }).get(SettingsViewModel::class.java)
    }
    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private var pushStatus = PreferenceManager.getBoolean(AppConstants.Pref_Key.PUSH_NOTIFICATION)
    private var inAppStatus = PreferenceManager.getBoolean(AppConstants.Pref_Key.IN_APP_NOTIFICATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setViews()
        setListener()
        setObserver()
    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, this)
        viewModel._settingLiveData.observe(this){

            saveDataInPreference(
                    pushNotificationStatus = pushStatus,
                    inAppNotification = inAppStatus,
            )
        }
    }

    private fun setListener() {
        ivBack.setOnClickListener(this)

    }

    private fun setViews() {
        tvTitle.text = getString(R.string.title_notification)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when(p0?.id){
            R.id.toggle_push ->{
                pushStatus = p1

            }else -> inAppStatus = p1
        }
        viewModel.setSettingRequest(
                inAppStatus = inAppStatus,
                pushNotification = pushStatus
        )
        viewModel.updateSettings()
    }
}