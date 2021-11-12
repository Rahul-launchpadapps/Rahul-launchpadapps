package com.app.okra.ui.add_medication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.models.MedicationData
import com.app.okra.utils.Listeners
import com.app.okra.utils.getDateFromISOInString
import kotlinx.android.synthetic.main.row_medication.view.*

class MedicineAdapter(
    var listener: Listeners.ItemClickListener,
    private val dataList: ArrayList<String>,
) : RecyclerView.Adapter<MedicineAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_medication, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(dataList[position], position)

        holder.itemView.setOnClickListener {
            listener.onSelect(position, dataList[position])
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(data: String?, position: Int) {

            data?.let { it ->

                if (it.isNotEmpty()) {
                    itemView.tvTitle.text = it
                }

            }
        }
    }
}
