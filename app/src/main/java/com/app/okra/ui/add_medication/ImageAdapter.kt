package com.app.okra.ui.add_medication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.okra.R
import com.app.okra.extension.loadUserImageFromUrl
import com.app.okra.utils.Listeners
import kotlinx.android.synthetic.main.row_image.view.*

class ImageAdapter (
    val context: Context,
    private val data : ArrayList<String>,
                    val listener :Listeners.ItemClickListener?=null
) : RecyclerView.Adapter<ImageAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_image, parent, false
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

           itemView.ivImage.loadUserImageFromUrl(context,data[position])
            itemView.ivCross.setOnClickListener{
                listener?.onSelect(position,null)
            }

        }
    }
}
