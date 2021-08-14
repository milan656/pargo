package com.tntra.pargo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo.R
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.generes.Genre

class ExploreGeneresDetailAdapter(var list: ArrayList<String>,
                                  var context: Context,
                                  var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<ExploreGeneresDetailAdapter.Viewholder>() {
    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreGeneresDetailAdapter.Viewholder {

        val view = LayoutInflater.from(context).inflate(R.layout.explore_generes_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: ExploreGeneresDetailAdapter.Viewholder, position: Int) {

        holder.tvgeneres.text = list[position]

        holder.tvgeneres.setOnClickListener {
            positionClick.onPositionClick(position, 4)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvgeneres: TextView = itemView.findViewById(R.id.tvgeneres)
    }
}