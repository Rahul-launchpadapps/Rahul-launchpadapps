package com.app.okra.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.app.okra.R
import com.app.okra.databinding.DownloadDialogBinding

class DownloadDialogFragment: DialogFragment() {
    private val reportsViewModel by activityViewModels<ReportsViewModel>()
    private lateinit var downloadDialogBinding: DownloadDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        downloadDialogBinding = DataBindingUtil.inflate(inflater, R.layout.download_dialog, container, false)
        return downloadDialogBinding.root
    }
}