package com.app.okra.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.ui.add_meal.AddMealActivity
import com.app.okra.ui.dashboard.HomeViewPagerAdapter
import com.app.okra.ui.logbook.LogbookFragment
import com.app.okra.ui.profile.ProfileFragment
import com.app.okra.utils.Listeners
import com.app.okra.utils.showAddNewDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashBoardActivity : BaseActivity(), Listeners.CustomDialogListener {

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
        homeViewPagerAdapter?.addFragment(InSightFragment())
        homeViewPagerAdapter?.addFragment(LogbookFragment())
        homeViewPagerAdapter?.addFragment(InSightFragment())
        homeViewPagerAdapter?.addFragment(ProfileFragment())
        view_pager_home.setAdapter(homeViewPagerAdapter)
        view_pager_home.setOffscreenPageLimit(4)
    }

    private fun setListeners() {
        bottom_navigation.setItemIconTintList(null)
        bottom_navigation.setOnNavigationItemSelectedListener(
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
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
                    return true
                }
            })

        fab.setOnClickListener {
            showAddNewDialog(this, this)
        }
    }

    override fun onImageClick(dialog: DialogInterface?) {

    }

    override fun onUploadFromGallery(dialog: DialogInterface?) {
        startActivity(Intent(this, AddMealActivity::class.java))
    }

    override fun onCancelOrUploadFromEmail(dialog: DialogInterface?) {
    }

}
