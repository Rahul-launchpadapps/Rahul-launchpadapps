package com.app.okra.ui.add_meal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseActivity
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.AddMealRepoImpl
import com.app.okra.extension.beVisible
import com.app.okra.extension.loadUserImageFromUrl
import com.app.okra.extension.viewModelFactory
import com.app.okra.models.CommonData
import com.app.okra.models.FoodItemsRequest
import com.app.okra.models.FoodRecognintionResponse
import com.app.okra.models.Items
import com.app.okra.ui.add_meal.contract.AddMealContracts
import com.app.okra.utils.*
import com.app.okra.utils.AppConstants.DateFormat.DATE_FORMAT_1
import com.google.gson.Gson
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_meal.*
import kotlinx.android.synthetic.main.activity_add_meal.cv_image
import kotlinx.android.synthetic.main.activity_add_meal.tvDate
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
    private var mHour: Int = 0
    private var mMin: Int = 0
    private var image: String = ""
    private  lateinit var selectedFoodItem : Items

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                AddMealViewModel(AddMealRepoImpl(apiServiceCalorieMama,apiServiceAuth))
            }
        ).get(AddMealViewModel::class.java)
    }

    private val activityForResult = registerForActivityResult(AddMealContracts()){ result ->
        if(result!=null){
            selectedFoodItem= result
            tvFoodTypeValue.text = result.name
            result.nutrition?.let {
                if(it.calories!=null) {
                    tvCalories.setText("${it.calories}")
                }
                if(it.totalCarbs!=null) {
                    tvCarbs.setText("${it.totalCarbs}")
                }

                if(it.totalFat!=null) {
                    tvFat.setText("${it.totalFat}")
                }

                if(it.protein!=null) {
                    tvProtein.setText("${it.protein}")
                }
            }
        }
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
            checkDataExistence()

        }

        cv_image.setOnClickListener {
            showOptionDialog(this, this, false)
        }
        tvUploadImage.setOnClickListener {
            showOptionDialog(this, this, false)
        }

        btnSave.setOnClickListener {
            when {
                TextUtils.isEmpty(image) -> {
                    showToast(getString(R.string.please_select_image))
                }
                TextUtils.isEmpty(tvDate.text.toString()) -> {
                    showToast(getString(R.string.please_select_date))
                }
                TextUtils.isEmpty(tvCalories.text.toString()) -> {
                    showToast(getString(R.string.please_select_calories))
                }
                TextUtils.isEmpty(tvCarbs.text.toString()) -> {
                    showToast(getString(R.string.please_select_carbs))
                }
                TextUtils.isEmpty(tvFat.text.toString()) -> {
                    showToast(getString(R.string.please_select_fat))
                }
                TextUtils.isEmpty(tvProtein.text.toString()) -> {
                    showToast(getString(R.string.please_select_protein))
                }
                else -> {
                    var date = ""
                    val foodList: ArrayList<FoodItemsRequest> = ArrayList()

                    date = tvDate.text.toString()

                    val foodItemsRequest = if(this::selectedFoodItem.isInitialized){
                        FoodItemsRequest(
                            selectedFoodItem.group,
                            selectedFoodItem.name,
                            selectedFoodItem.selectedServingSize?.unit

                        )
                    }else {
                        FoodItemsRequest(
                            "jshjksd",
                            "djhjh",
                            "1"
                        )
                    }
                    foodList.add(foodItemsRequest)

                    val dateToConvert =   getDifferentInfoFromDate(date,DATE_FORMAT_1,DATE_FORMAT_1)
                    viewModel.prepareAddRequest(
                        date = getISOFromDateAndTime_inString(dateToConvert),
                        image = image,
                        foodItems = foodList,
                        foodType = tvFoodTypeValue.text.toString(),
                        calories = CommonData(tvCalories.text.toString(), "cal"),
                        carbs = CommonData(tvCarbs.text.toString(), "gm"),
                        fat = CommonData(tvFat.text.toString(), "gm"),
                        protein = CommonData(tvProtein.text.toString(), "gm"),
                    )
                    viewModel.addMeal()
                }
            }
        }

        tvDate.setOnClickListener {
            selectDate(tvDate)
        }
    }

    private fun checkDataExistence() {
        if(image.isNotEmpty()){
            showCustomAlertDialog(
                this,
                object : Listeners.DialogListener{
                    override fun onOkClick(dialog: DialogInterface?) {
                        finish()
                        dialog?.dismiss()
                    }

                    override fun onCancelClick(dialog: DialogInterface?) {
                        dialog?.dismiss()
                    }

                },
                MessageConstants.Messages.unsaved_meal_data,
                true,
                positiveButtonText = getString(R.string.ok),
                negativeButtonText = getString(R.string.cancel),
                title = getString(R.string.unsaved_meal)
            )
        }else{
            finish()
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
        if (mPermissionUtils.checkAndGetStorageAndCameraPermissions(this)) {
            onPermissionsGiven(AppConstants.PermissionCodes.PERMISSION_CAMERA)
        }
    }

    private fun checkStoragePermissions() {
        typeOfAction = AppConstants.PermissionCodes.PERMISSION_STORAGE
        if (mPermissionUtils.checkAndGetStoragePermissions(this)) {
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
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)

                    if (result != null && result.uri != null) {
                        val imageUri = result.uri!!
                        val fileSize = getFileSize(imageUri!!)

                        println(":::: File Size: $fileSize")
                        if (fileSize > -1 && fileSize <= AppConstants.ALLOWED_FILE_SIZE) {
                            image = imageUri.path.toString()
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
            .setRequestedSize(50, 50)
            .start(this)

    }

    private fun setObserver() {
        setBaseObservers(viewModel, this, this, observeError = false)
        viewModel._foodRecognitionLiveData.observe(this) {
            iv_image.loadUserImageFromUrl(this, image)
            try {
                println("::: Output: ${it}")

                /* val jObject = JSONObject(it)

                 val jsonString = jObject.getString("lang")
                 val response = Gson().fromJson(it, FoodRecognintionResponse::class.java)*/
                val mealInput  = if (!it.is_food) {
                    println("::::: Invalid: True ")

                    MealInput(invalid = true, image= image)
                } else if (it.results == null || it.results!!.size == 0) {
                    println("::::: Invalid: True, No Data ")

                    MealInput(invalid = true, image= image)
                } else {
                    MealInput(invalid = false, image= image, data = it)
                }
                activityForResult.launch(mealInput)

                println("::: Exception NO")

                /*startActivity(Intent(this, ImageViewActivity::class.java)
                    .putExtra(AddMealContracts.data,mealInput))*/

            }catch (e: Exception){
                println("::: Exception: ${e.message}")
                e.printStackTrace()
            }
        }

        viewModel._addMealLiveData.observe(this) {
            finish()
        }

        viewModel._errorObserver.observe(this) {
            val data = it.getContent()!!
            showToast(data.message!!)
        }
    }

    private fun selectDate(tvDate: AppCompatTextView) {
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                var strDate: String =
                    year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString()
                showTimePicker(tvDate, strDate)
            }, mYear, mMonth, mDay)
        val c1 = Calendar.getInstance()
        c1.add(Calendar.MONTH, -2)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()

    }

    private fun showTimePicker(
        tvDate: AppCompatTextView,
        selectedDate: String
    ) {
        val c = Calendar.getInstance()
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMin = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(
            this, { timePicker, hour, minute ->
               val completeDate = "$selectedDate $hour:$minute"
                tvDate.text =  getDifferentInfoFromDate_String(completeDate,"yyyy-MM-dd hh:mm",DATE_FORMAT_1)
            },
            mHour,
            mMin,
            true
        )

        tpd.show()

    }

    override fun onBackPressed() {
        checkDataExistence()
    }
}