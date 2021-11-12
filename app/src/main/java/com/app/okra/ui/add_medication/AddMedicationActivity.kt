package com.app.okra.ui.add_medication

import android.os.Bundle
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel

class AddMedicationActivity : BaseActivity() {

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medication)
    }
}