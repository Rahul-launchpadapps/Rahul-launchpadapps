package com.app.okra.ui.logbook.add_meal

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.extension.beVisible
import com.app.okra.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_meal.*
import kotlinx.android.synthetic.main.layout_header.*
import java.io.File

class AddMealActivity : BaseActivity(), Listeners.CustomDialogListener,
    PermissionUtils.IGetPermissionListener,
    ImageUtils.IChooseImageInterface {

    private val mPermissionUtils: PermissionUtils = PermissionUtils(this)
    private val mChooseImageUtils: ImageUtils = ImageUtils()
    private var typeOfAction: Int = 0

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)
        setUpToolbar()
        setListener()
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.new_meal)
        btnSave.beVisible()
        btnSave.text = getString(R.string.add_meal)
    }

    private fun setListener() {
        mChooseImageUtils.setCallbacks(this, this)
       // viewModel.setAmazonCallback(this)

        ivBack.setOnClickListener {
            finish()
        }

        cv_image.setOnClickListener {
            showOptionDialog(this, this, false)
        }

        btnSave.setOnClickListener {
           // navController.navigate(R.id.action_editTestDetails_to_successfulUpdatedFragment, null)
        }
    }

    override fun onImageClick(dialog: DialogInterface?) {
        checkCameraPermissions()
        dialog?.dismiss()
    }

    override fun onUploadFromGallery(dialog: DialogInterface?) {
        checkStoragePermissions()
        dialog?.dismiss()
    }

    override fun onCancelOrUploadFromEmail(dialog: DialogInterface?) {
        dialog?.dismiss()
    }

    private fun checkCameraPermissions() {
        typeOfAction = AppConstants.PermissionCodes.PERMISSION_CAMERA
        if(mPermissionUtils.checkAndGetStorageAndCameraPermissions(this)){
            onPermissionsGiven(AppConstants.PermissionCodes.PERMISSION_CAMERA)
        }
    }

    private fun checkStoragePermissions() {
        typeOfAction = AppConstants.PermissionCodes.PERMISSION_STORAGE
        if(mPermissionUtils.checkAndGetStoragePermissions(this)) {
            onPermissionsGiven(AppConstants.PermissionCodes.PERMISSION_STORAGE)
        }

    }

    override fun onPermissionsGiven(data: Int) {
        when (data) {
            AppConstants.PermissionCodes.PERMISSION_STORAGE -> {
                mChooseImageUtils.openGallery()
            }
            AppConstants.PermissionCodes.PERMISSION_CAMERA -> {
                mChooseImageUtils.openCamera()
            }
        }
    }

    override fun onPermissionsDeny(requestCode: Int) {
        when (requestCode) {
            AppConstants.PermissionCodes.PERMISSION_STORAGE -> {
                showToast(MessageConstants.Messages.msg_storage_permission)
            }
            AppConstants.PermissionCodes.PERMISSION_CAMERA -> {
                showToast(MessageConstants.Messages.msg_camera_permission)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)

                    if (result != null && result.uri != null) {

                        val imageUri = result.uri!!

                        val fileSize = getFileSize(imageUri!!)
                        println(":::: File Size: $fileSize")
                        if (fileSize > -1 && fileSize <= AppConstants.ALLOWED_FILE_SIZE) {
                          //  viewModel.uploadFile(imageUri)
                        } else {
                            showToast("Selected file exceeds the maximum limit of ${AppConstants.ALLOWED_FILE_SIZE} MB.")
                        }
                    }
                }
                else -> {
                    mChooseImageUtils.setImageResult(requestCode, resultCode, data)

                }
            }
        }
    }


    override fun setImagePath(uri: Uri) {
        val file = File(uri.path)

        if (file.exists()) {
            openCropper(file.toUri())
        }
    }

    override fun setImageForGallery(path: Uri) {
        openCropper(path)
    }

    private fun openCropper(uri: Uri) {
        CropImage.activity(uri)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setAspectRatio(1, 1)
            .setAutoZoomEnabled(false)
            .setAllowFlipping(false)
            .setBorderLineThickness(8f)
            .setGuidelines(CropImageView.Guidelines.OFF)
            .setAllowRotation(false)
            .start(this)

    }
}