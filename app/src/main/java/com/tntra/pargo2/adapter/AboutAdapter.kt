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

class AboutAdapter(var list: ArrayList<String>,
                   var context: Context,
                   var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<AboutAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.about_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}