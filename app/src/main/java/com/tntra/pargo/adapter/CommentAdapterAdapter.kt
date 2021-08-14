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

class CommentAdapterAdapter(var list: ArrayList<Genre>,
                            var context: Context,
                            var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<CommentAdapterAdapter.Viewholder>() {
    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapterAdapter.Viewholder {

        val view = LayoutInflater.from(context).inflate(R.layout.explore_generes_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapterAdapter.Viewholder, position: Int) {

        holder.tvgeneres.text = list[position].attributes.name
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvgeneres: TextView = itemView.findViewById(R.id.tvgeneres)
    }
}