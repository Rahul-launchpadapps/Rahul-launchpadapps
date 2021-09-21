package com.app.okra.ui.add_meal

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.extension.loadUserImageFromUrl
import com.app.okra.models.FoodRecognintionResponse
import com.app.okra.utils.Listeners
import com.app.okra.utils.showCustomAlertDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_image_view.*
import kotlinx.android.synthetic.main.bottomsheet_choose_item.*

class ImageViewActivity : BaseActivity(), Listeners.DialogListener {

    var data: FoodRecognintionResponse? = null

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        getData()
    }

    private fun getData() {
        intent.let {
            iv_image.loadUserImageFromUrl(this, intent.getStringExtra("image"))
            if (intent.getBooleanExtra("invalid", false)) {
                showCustomAlertDialog(
                    this,
                    this,
                    getString(R.string.this_does_not_look_like_photo),
                    false,
                    positiveButtonText = getString(R.string.retake),
                    negativeButtonText = "",
                    title = getString(R.string.invalid_photo)
                )
            } else {
                data = intent.getParcelableExtra("data")!!
                showBottomSheetDialog()
            }
        }
    }

    override fun onOkClick(dialog: DialogInterface?) {
        dialog?.dismiss()
        setResult(100)
        finish()
    }

    override fun onCancelClick(dialog: DialogInterface?) {
        dialog?.dismiss()
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.apply {
            setContentView(R.layout.bottomsheet_logs_filter)
            setupFullHeight(bottomSheetDialog)
            show()

            val imageAdapter: FoodItemNameAdapter
            val layountManager =
                LinearLayoutManager(bottomSheetDialog.context, LinearLayoutManager.HORIZONTAL, true)
            if (data?.results != null) {
                imageAdapter = data?.results.let {
                    it?.let { it1 ->
                        FoodItemNameAdapter(
                            it1
                        )
                    }
                }!!
                rv_item_names.layoutManager = layountManager
                rv_item_names.adapter = imageAdapter
            }
        }
    }

    private fun setupFullHeight(bottomSheet: BottomSheetDialog) {
        val parentLayout =
            bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        parentLayout?.let { it ->
            val behaviour = BottomSheetBehavior.from(it)
            val layoutParams = it.layoutParams
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.layoutParams = layoutParams
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

}