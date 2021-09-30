package com.app.okra.ui.add_meal

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.AddMealRepoImpl
import com.app.okra.extension.beVisible
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.FoodRecognintionResponse
import com.app.okra.utils.*
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_meal.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_header.tvTitle
import java.io.File
import java.util.*
import org.json.JSONObject




class AddMealActivity : BaseActivity(), Listeners.CustomDialogListener,
    PermissionUtils.IGetPermissionListener,
    ImageUtils.IChooseImageInterface {

    private val mPermissionUtils: PermissionUtils = PermissionUtils(this)
    private val mChooseImageUtils: ImageUtils = ImageUtils()
    private var typeOfAction: Int = 0
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var image: String = ""

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                AddMealViewModel(AddMealRepoImpl(apiServiceCalorieMama))
            }
        ).get(AddMealViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)
        setUpToolbar()
        setObserver()
        setListener()
    }

    private fun setUpToolbar() {
        tvTitle.text = getString(R.string.new_meal)
        btnSave.beVisible()
        btnSave.text = getString(R.string.add_meal)
    }

    private fun setListener() {
        mChooseImageUtils.setCallbacks(this, this)

        ivBack.setOnClickListener {
            finish()
        }

        cv_image.setOnClickListener {
            showOptionDialog(this, this, false)
        }

        btnSave.setOnClickListener {
           // navController.navigate(R.id.action_editTestDetails_to_successfulUpdatedFragment, null)
        }

        tvDate.setOnClickListener {
            selectDate(tvDate)
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
                        image = imageUri.path.toString()
                        println(":::: File Size: $fileSize")
                        if (fileSize > -1 && fileSize <= AppConstants.ALLOWED_FILE_SIZE) {
                            viewModel.foodRecognition(imageUri.path)
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
            .setRequestedSize(50,50)
            .start(this)

    }

    private fun setObserver() {
        setBaseObservers(viewModel, this,this, observeError = false)
        viewModel._foodRecognitionLiveData.observe(this) { it
            val jObject = JSONObject(it.toString())
            val aJsonString = jObject.getString("lang")
            val response =
                Gson().fromJson(Gson().toJson(it), FoodRecognintionResponse::class.java)
            if(!response.is_food) {
                startActivityForResult(Intent(this,ImageViewActivity::class.java)
                    .putExtra("invalid",true)
                    .putExtra("image",image)
                    ,100
                )
            }
            else if(response.results?.size==null || response.results?.size==0) {
                startActivityForResult(Intent(this,ImageViewActivity::class.java)
                    .putExtra("invalid",true)
                    .putExtra("image",image)
                    ,100
                )
            }
            else {
                startActivityForResult(Intent(this,ImageViewActivity::class.java)
                    .putExtra("invalid",false)
                    .putExtra("image",image)
                    .putExtra("data",response)
                    ,100
                )
            }
        }

        viewModel._errorObserver.observe(this){
            val data = it.getContent()!!
            showToast(data.message!!)
        }
    }

    private fun selectDate(tvFromDate: AppCompatTextView) {
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                val strDate: String =
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                tvFromDate.text = strDate
            }, mYear, mMonth, mDay)
        val c1 = Calendar.getInstance()
        c1.add(Calendar.MONTH, -2)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()

    }

}