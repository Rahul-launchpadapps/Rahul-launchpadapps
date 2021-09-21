package com.app.okra.ui.add_meal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.models.Results
import kotlinx.android.synthetic.main.row_item_name.view.*

class FoodItemNameAdapter (private val data : ArrayList<Results>
) : RecyclerView.Adapter<FoodItemNameAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_item_name, parent, false
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

            itemView.tv_name.text = data[position].group

            val adapter = data[position].items?.let { FoodItemAdapter(it) }
            itemView.rvFoodItem.adapter = adapter
        }
    }
}
