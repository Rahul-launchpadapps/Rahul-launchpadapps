package com.app.okra.ui.add_medication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.okra.R
import com.app.okra.base.BaseFragment
import com.app.okra.base.BaseViewModel
import com.app.okra.data.repo.MedicationRepoImpl
import com.app.okra.extension.viewModelFactory
import com.app.okra.ui.logbook.medication.MedicationViewModel
import com.app.okra.utils.Listeners
import kotlinx.android.synthetic.main.fragment_search_medication.*
import kotlinx.android.synthetic.main.fragment_search_medication.rv_medication
import kotlinx.android.synthetic.main.layout_header.*

class SearchMedicationFragment : BaseFragment(), Listeners.ItemClickListener {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var medicationAdapter: MedicineAdapter

    /*Empagliflozin
    Ertugliflozin
    Pioglitazone and Metformin
    Rosiglitazone and Metformin
    Rosiglitazone and Glimepiride
    Pioglitazone and Glimepiride
    Glyburide and Metformin
    Empagliflozin and Linagliptin
    Canagliflozin and Metformin
    Sitagliptin and Metformin
    Linagliptin and Metformin
    Alogliptin and Metformin
    Saxagliptin and Metformin
    Glipizide and Metformin
    Alogliptin and Pioglitazone
    Repaglinide and Metformin
    Dapagliflozin and Metformin
    Lixisenatide
    Exenatide
    Exenatide
    Semaglutide
    Albiglutide
    Dulaglutide
    Liraglutide
    Pramlintide acetate*/
    private val medicine = arrayOf("Repaglinide", "Nateglinide", "Miglitol","Acarbose","Pioglitazone","Rosiglitazone","" +
            "Sitagliptin","Saxagliptin","Alogliptin","Linagliptin","Glimepiride","Glyburide","Chlorpropamide","Glipizide",
    "Tolbutamide","Tolazamide","Metformin","Bromocriptine","Colesevelam","Dapagliflozin","Canagliflozin")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_medication, container, false)
    }

    override fun getViewModel(): BaseViewModel? {
        return viewModel
    }

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory {
                MedicationViewModel(MedicationRepoImpl(apiServiceAuth))
            }
        ).get(MedicationViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setViews()
        setListener()
    }

    private fun setAdapter() {
      //  medicationAdapter = MedicineAdapter(this, rhjk)
     /*   layoutManager = LinearLayoutManager(requireContext())
        rv_medication.layoutManager = layoutManager
        rv_medication.adapter = medicationAdapter*/
    }

    private fun setViews() {
        tvTitle.text = getString(R.string.medication)
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            activity?.finish()
        }

        tvAddMed.setOnClickListener {
            navController.navigate(R.id.action_searchMed_to_addMed, null)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               /* if(p0?.length!! ==0){

                }else if(p0?.length!! >3){

                }*/
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    override fun onSelect(o: Any?, o1: Any?) {

    }

    override fun onUnSelect(o: Any?, o1: Any?) {

    }
}