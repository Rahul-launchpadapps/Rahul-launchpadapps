package com.app.okra.ui.add_medication

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.utils.dialog
import kotlinx.android.synthetic.main.fragment_add_medication.*
import kotlinx.android.synthetic.main.layout_button.view.*
import kotlinx.android.synthetic.main.layout_header.*

class AddMedicationFragment : BaseFragment() {

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_medication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        setListener()
    }

    private fun setViews() {
        tvTitle.text = getString(R.string.add_medicine)
        layout_button.btnCommon.text = getString(R.string.next)
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.finish()
        }

        layout_button.btnCommon.setOnClickListener {
           if(etName.text.isNullOrEmpty()){
               showToast(getString(R.string.please_enter_medicine_name))
           }else
               showUnitDialog()
        }
    }

    fun showUnitDialog() {
        dialog = activity?.let { Dialog(it, R.style.MyCustomTheme) }
        val view: View = LayoutInflater.from(activity).inflate(R.layout.dialog_medicine_unit, null)
        dialog?.apply {
            setContentView(view)
            setCanceledOnTouchOutside(false)

            val lp = dialog!!.window!!.attributes
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            lp.dimAmount = 0.5f
            window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            lp.windowAnimations = R.style.DialogAnimation
            window?.attributes = lp

            val btnAdd: Button = findViewById(R.id.btnAdd)
            val tvTitle: TextView = findViewById(R.id.tvTitle)
            val tvMG: TextView = findViewById(R.id.tvMG)
            val tvPill: TextView = findViewById(R.id.tvPill)

          //  tvTitle.text = etName.text.toString()

            tvMG.setOnClickListener {
                tvMG.background = resources.getDrawable(R.drawable.bg_button_green)
                tvMG.setTextColor(ContextCompat.getColor(context,R.color.white))
                tvPill.background = null
                tvPill.setTextColor(ContextCompat.getColor(context,R.color.grey_3))
            }

            tvPill.setOnClickListener {
                tvPill.background = resources.getDrawable(R.drawable.bg_button_green)
                tvPill.setTextColor(ContextCompat.getColor(context,R.color.white))
                tvMG.background = null
                tvMG.setTextColor(ContextCompat.getColor(context,R.color.grey_3))
            }

            btnAdd.setOnClickListener {
                dialog?.dismiss()
            }
            show()
        }
    }

}