package com.app.okra.ui.logbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.models.Data
import com.app.okra.models.TestListResponse
import com.app.okra.utils.AppConstants
import com.app.okra.utils.Listeners
import com.app.okra.utils.getDateFromISOInString
import kotlinx.android.synthetic.main.row_test_logs.view.*

class TestLogsAdapter (var listener: Listeners.ItemClickListener,
                       private val dataList : List<Data>,
) : RecyclerView.Adapter<TestLogsAdapter.ItemViewHolder>() {

    lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_test_logs, parent, false
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
                    itemView.tvGlucoseValue.text = it + " mg/dL"
                }
                it.testingTime?.let{
                    if(it.equals(AppConstants.Testing_Time.AFTER_MEAL))
                        itemView.tvDetail.text = context.getString(R.string.after_meal)
                    else if(it.equals(AppConstants.Testing_Time.BEFORE_MEAL))
                        itemView.tvDetail.text = context.getString(R.string.before_meal)
                    else
                        itemView.tvDetail.text = it
                }

                it.createdAt?.let{
                    if(it.isNotEmpty()) {
                        itemView.tvTime.text = getDateFromISOInString(it, formatYouWant = "hh:mm a")
                    }
                }

            }
        }
    }
}
