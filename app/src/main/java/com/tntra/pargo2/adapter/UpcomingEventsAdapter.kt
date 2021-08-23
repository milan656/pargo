package com.tntra.pargo2.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo2.R
import com.tntra.pargo2.common.onClickAdapter
import kotlin.math.roundToInt

class UpcomingEventsAdapter(var list: ArrayList<String>,
                            var context: Context,
                            var onpositionClick: onClickAdapter?,
                            var width: Int) : RecyclerView.Adapter<UpcomingEventsAdapter.Viewholder>() {

    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_event_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val widthfinal: Int = (width / 2.9).roundToInt()
        Log.e("TAGG", "onBindViewHolder: $widthfinal")
        val params = LinearLayout.LayoutParams(
                widthfinal,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 15, 0)
        holder.llcontent.setLayoutParams(params)

        if (position == 0) {
            holder.tvplus?.visibility = View.VISIBLE
            holder.llcontent?.setOnClickListener {
                positionClick.onPositionClick(position, 10)
            }
        } else {
            holder.tvplus?.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var llcontent = itemView.findViewById<LinearLayout>(R.id.llcontent)
        var tvplus = itemView.findViewById<TextView>(R.id.tvplus)
    }
}