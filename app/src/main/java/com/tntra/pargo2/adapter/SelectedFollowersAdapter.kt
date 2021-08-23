package com.tntra.pargo2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo2.R
import com.tntra.pargo2.common.onClickAdapter
import com.tntra.pargo2.model.SelectedFollowrs

class SelectedFollowersAdapter(var list: ArrayList<SelectedFollowrs>,
                               var context: Context,
                               var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<SelectedFollowersAdapter.Viewholder>() {
    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedFollowersAdapter.Viewholder {

        val view = LayoutInflater.from(context).inflate(R.layout.selected_followers_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: SelectedFollowersAdapter.Viewholder, position: Int) {

        holder.tvName.text = list[position].name

        holder.ivRemove.setOnClickListener {
            onpositionClick?.onPositionClick(position, 4)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var ivRemove: ImageView = itemView.findViewById(R.id.ivRemove)
    }
}