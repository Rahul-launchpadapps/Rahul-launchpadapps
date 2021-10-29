package com.app.okra.ui.logbook.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.models.Data
import com.app.okra.utils.Listeners
import com.app.okra.utils.getDateFromISOInString
import com.app.okra.utils.getMealTime
import kotlinx.android.synthetic.main.row_test.view.*

class TestsAdapter (var listener: Listeners.ItemClickListener,
                    private val dataList :  ArrayList<Data>,
) : RecyclerView.Adapter<TestsAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_test, parent, false
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

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun onBind(data: Data?, position: Int) {

            data?.let{ it ->
                it.bloodGlucose?.let{
                    itemView.tvGlucoseValue.text = "$it mg/dL"
                }
                it.testingTime?.let{
                    itemView.tvDetail.text = getMealTime(it)
                }

                it.date?.let{
                    if(it.isNotEmpty()) {
                        itemView.tvTime.text = getDateFromISOInString(it, formatYouWant = "hh:mm a")
                    }
                }

            }
        }
    }
}
