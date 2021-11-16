package com.app.okra.ui.add_medication

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.ui.add_meal.AddMealViewModel
import com.app.okra.utils.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_edit_medication.*
import kotlinx.android.synthetic.main.layout_header.*
import java.io.File

class EditMedicationFragment : BaseFragment(),
    View.OnClickListener,
    Listeners.CustomMediaDialogListener,
    PermissionUtils.IGetPermissionListener,
    ImageUtils.IChooseImageInterface {

    private lateinit var mAdapter: ImageAdapter
    private lateinit var localImageUri: Uri
    private val mPermissionUtils: PermissionUtils = PermissionUtils(this)
    private val mChooseImageUtils: ImageUtils = ImageUtils()
    private var typeOfAction: Int = 0
    private val imageList = ArrayList<String>()


    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            /*viewModelFactory {
                AddMealViewModel(AddMealRepoImpl(apiServiceCalorieMama,apiServiceAuth))
            }*/
        ).get(AddMealViewModel::class.java)
    }

    private val activityGalleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result!=null){
            val fetchedData: CropImage.ActivityResult? = CropImage.getActivityResult(result.data)

            if (fetchedData != null && fetchedData.uri != null) {
                val imageUri = fetchedData.uri!!
                val fileSize = getFileSize(imageUri!!)

                println(":::: File Size: $fileSize")
                if (fileSize > -1 && fileSize <= AppConstants.ALLOWED_FILE_SIZE) {
                    localImageUri = imageUri
                    viewModel.uploadFile(imageUri)
                    //  viewModel.foodRecognition(imageUri.path)
                } else {
                    showToast("Selected file exceeds the maximum limit of ${AppConstants.ALLOWED_FILE_SIZE} MB.")
                }
            }
        }
    }

    private val activityCameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it != null) {
                    mChooseImageUtils.getCameraImageResult(it.data)
                }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_medication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setObserver()
        setListener()
        setAdapter()
    }

    private fun setAdapter() {
        mAdapter = ImageAdapter(requireContext(), imageList)
        rvMealImages.adapter = mAdapter
    }


    private fun setListener() {
        mChooseImageUtils.setCallbacks(requireActivity(), this, this)
        viewModel.setAmazonCallback(requireActivity())

        ivBack.setOnClickListener(this)

        cvAdd.setOnClickListener(this)

      /*  btnSave.setOnClickListener {
            when {
                TextUtils.isEmpty(amazonImageUrl) -> {
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
                        image = amazonImageUrl,
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
        }*/

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
        if (mPermissionUtils.checkAndGetStorageAndCameraPermissions(requireContext())) {
            onPermissionsGiven(AppConstants.PermissionCodes.PERMISSION_CAMERA)
        }
    }

    private fun checkStoragePermissions() {
        typeOfAction = AppConstants.PermissionCodes.PERMISSION_STORAGE
        if (mPermissionUtils.checkAndGetStoragePermissions(requireContext())) {
            onPermissionsGiven(AppConstants.PermissionCodes.PERMISSION_STORAGE)
        }
    }

    override fun onPermissionsGiven(data: Int) {
        when (data) {
            AppConstants.PermissionCodes.PERMISSION_STORAGE -> {
             //   mChooseImageUtils.openGallery()
              val intent =  Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                }
                activityGalleryResult.launch(intent)

            }
            AppConstants.PermissionCodes.PERMISSION_CAMERA -> {
              val cameraIntent =   mChooseImageUtils.openCamera()
                activityCameraResult.launch(cameraIntent)
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
                            localImageUri = imageUri
                            viewModel.uploadFile(imageUri)
                          //  viewModel.foodRecognition(imageUri.path)
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
            openCropper(file as Uri)
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
            .start(requireActivity())

    }

    private fun setObserver() {
        setBaseObservers(viewModel, this)

        viewModel._amazonStatusLiveData.observe(viewLifecycleOwner) {
            if (it.serverUrl.isNotEmpty()) {
                imageList.add(it.serverUrl)
                mAdapter.notifyDataSetChanged()
                rvMealImages.scrollToPosition(0)
            } else {
                showToast(MessageConstants.Errors.an_error_occurred)
            }
        }
    }


    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.cvAdd ->{
                showOptionDialog(requireContext(), this, false)

            }
        }
    }

}