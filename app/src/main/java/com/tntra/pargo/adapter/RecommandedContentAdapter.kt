package com.tntra.pargo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo.R
import com.tntra.pargo.common.onClickAdapter

class RecommandedContentAdapter(var item: ArrayList<String>?,
                                var context: Context,
                                var onPositionClick: onClickAdapter?

) : RecyclerView.Adapter<RecommandedContentAdapter.ViewHolder>() {
    private val positionClick: onClickAdapter = onPositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommanded_content_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 0) {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 0)
            holder.llEventContent.setLayoutParams(params)
        } else {
            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 35, 0, 0)
            holder.llEventContent.setLayoutParams(params)

        }
    }

    override fun getItemCount(): Int {
        return item?.size!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var llEventContent = itemView.findViewById(R.id.llContent) as LinearLayout

    }
}