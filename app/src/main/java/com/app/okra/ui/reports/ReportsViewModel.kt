package com.app.okra.ui.reports

import androidx.lifecycle.MutableLiveData
import com.app.okra.base.BaseViewModel

class ReportsViewModel: BaseViewModel() {
    val fileTypeArray = arrayOf("FileType", "XLSX", "PDF")
    lateinit var selectedFileType: String
    var startDateTimestamp = MutableLiveData(0L)
    var endDateTimestamp = MutableLiveData(0L)
    val isStartDateSetMLD = MutableLiveData(false)
    val isEndDateSetMLD = MutableLiveData(false)
    val isFileTypeSetMLD = MutableLiveData(false)
    val areAllFieldsSet = MutableLiveData(false)
}