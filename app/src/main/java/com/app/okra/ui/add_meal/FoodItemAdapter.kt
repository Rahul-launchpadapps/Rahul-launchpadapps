package com.app.okra.ui.add_meal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.models.Items
import com.app.okra.models.Results
import kotlinx.android.synthetic.main.row_item_name.view.*
import kotlinx.android.synthetic.main.row_meal.view.*

class FoodItemAdapter (private val data : ArrayList<Items>
) : RecyclerView.Adapter<FoodItemAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_meal, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind( position)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun onBind( position: Int) {

            itemView.tvTitle.text = data[position].name
            itemView.tvDetail.text = data[position].servingSizes?.size.toString() + " Serving"
        }
    }
}
