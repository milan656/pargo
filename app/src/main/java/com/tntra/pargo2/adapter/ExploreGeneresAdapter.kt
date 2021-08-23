package com.tntra.pargo2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo2.R
import com.tntra.pargo2.common.onClickAdapter
import com.tntra.pargo2.model.generes.Genre

class ExploreGeneresAdapter(var list: ArrayList<Genre>,
                            var context: Context,
                            var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<ExploreGeneresAdapter.Viewholder>() {
    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreGeneresAdapter.Viewholder {

        val view = LayoutInflater.from(context).inflate(R.layout.explore_generes_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: ExploreGeneresAdapter.Viewholder, position: Int) {

        holder.tvgeneres.text = list[position].attributes.name
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvgeneres: TextView = itemView.findViewById(R.id.tvgeneres)
    }
}