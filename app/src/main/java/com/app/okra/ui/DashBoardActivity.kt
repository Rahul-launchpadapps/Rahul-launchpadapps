package com.app.okra.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.preference.PreferenceManager
import com.app.okra.ui.add_meal.AddMealActivity
import com.app.okra.ui.add_medication.AddMedicationActivity
import com.app.okra.ui.connected_devices.ConnectedDeviceActivity
import com.app.okra.ui.home.HomeViewPagerAdapter
import com.app.okra.ui.home.HomeFragment
import com.app.okra.ui.insight.InSightFragment
import com.app.okra.ui.logbook.LogbookFragment
import com.app.okra.ui.profile.ProfileFragment
import com.app.okra.utils.AppConstants
import com.app.okra.utils.Listeners
import com.app.okra.utils.showAddNewDialog
import com.app.okra.utils.showAlertDialog
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashBoardActivity : BaseActivity(), Listeners.CustomDialogListener, Listeners.DialogListener {

    private var homeViewPagerAdapter: HomeViewPagerAdapter? = null

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        init()
        setFragments()
        setListeners()
    }

    private fun init() {
        homeViewPagerAdapter = HomeViewPagerAdapter(supportFragmentManager, this, 0)
    }

    private fun setFragments() {
        homeViewPagerAdapter?.addFragment(HomeFragment())
        homeViewPagerAdapter?.addFragment(LogbookFragment())
        homeViewPagerAdapter?.addFragment(InSightFragment())
        homeViewPagerAdapter?.addFragment(ProfileFragment())
        view_pager_home.adapter = homeViewPagerAdapter
        view_pager_home.offscreenPageLimit = 4
    }

    private fun setListeners() {
        bottom_navigation.itemIconTintList = null
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    val menu: Menu = bottom_navigation.getMenu()
                    menu.getItem(0).setIcon(R.mipmap.home_active)
                    menu.getItem(1)
                        .setIcon(R.mipmap.logbook_inactive)
                    menu.getItem(3)
                        .setIcon(R.mipmap.insight_inactive)
                    menu.getItem(4)
                        .setIcon(R.mipmap.account_inactive)
                    view_pager_home.setCurrentItem(0)
                }
                R.id.action_logbook -> {
                    val menu1: Menu = bottom_navigation.getMenu()
                    menu1.getItem(0).setIcon(R.mipmap.home_inactive)
                    menu1.getItem(1)
                        .setIcon(R.mipmap.logbook_active)
                    menu1.getItem(3)
                        .setIcon(R.mipmap.insight_inactive)
                    menu1.getItem(4)
                        .setIcon(R.mipmap.account_inactive)
                    view_pager_home.setCurrentItem(1)
                }
                R.id.action_insight -> {
                    val menu2: Menu = bottom_navigation.getMenu()
                    menu2.getItem(0).setIcon(R.mipmap.home_inactive)
                    menu2.getItem(1)
                        .setIcon(R.mipmap.logbook_inactive)
                    menu2.getItem(3)
                        .setIcon(R.mipmap.insight_active)
                    menu2.getItem(4)
                        .setIcon(R.mipmap.account_inactive)
                    view_pager_home.setCurrentItem(2)
                }
                R.id.action_profile -> {
                    val menu3: Menu = bottom_navigation.getMenu()
                    menu3.getItem(0).setIcon(R.mipmap.home_inactive)
                    menu3.getItem(1)
                        .setIcon(R.mipmap.logbook_inactive)
                    menu3.getItem(3)
                        .setIcon(R.mipmap.insight_inactive)
                    menu3.getItem(4)
                        .setIcon(R.mipmap.account_active)
                    view_pager_home.setCurrentItem(3)
                }
            }
            true
        }

        fab.setOnClickListener {
            showAddNewDialog(this, this)
        }
    }

    override fun onFirstOptionClick(dialog: DialogInterface?) {
        if(PreferenceManager.getBoolean(AppConstants.Pref_Key.IS_VERIFIED)) {
            startActivity(Intent(this, ConnectedDeviceActivity::class.java))
        }else{
            showAlertDialog(
                this,
                this,
                getString(R.string.unverified_account_message),
                false,
                getString(R.string.ok),
                getString(R.string.ok),
                getString(R.string.alert)
            )
        }
    }

    override fun onSecondOptionClick(dialog: DialogInterface?) {
        startActivity(Intent(this, AddMealActivity::class.java))
    }

    override fun onThirdOptionClick(dialog: DialogInterface?) {
        startActivity(Intent(this, AddMedicationActivity::class.java))
    }

    override fun onOkClick(dialog: DialogInterface?) {
        dialog?.dismiss()
    }

    override fun onCancelClick(dialog: DialogInterface?) {}

}
