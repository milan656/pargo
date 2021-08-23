package com.tntra.pargo2.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tntra.pargo2.R
import com.tntra.pargo2.common.onClickAdapter

class LiveStreamListAdapter(var list: ArrayList<String>,
                            var context: Context,
                            var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<LiveStreamListAdapter.Viewholder>() {
    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveStreamListAdapter.Viewholder {

        val view = LayoutInflater.from(context).inflate(R.layout.stream_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: LiveStreamListAdapter.Viewholder, position: Int) {

        Log.e("TAGG", "onBindViewHolder: "+list.get(position) )
        try {
            Glide.with(context).load(list[position]).into(holder.ivMember)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivMember = itemView.findViewById<ImageView>(R.id.ivMember)
    }
}