package com.tntra.pargo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tntra.pargo.R
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.creatorListsearch.CreatorsListMdel
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class RecommandCreatorsAdapter(var list: ArrayList<CreatorsListMdel>,
                               var context: Context,
                               var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<RecommandCreatorsAdapter.Viewholder>() {
    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommand_creators_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.tvCreatorName.text = list[position].creatorname
        try {
            Glide.with(context).load(list[position].creator_image).into(holder.ivCreator)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCreator = itemView.findViewById<CircleImageView>(R.id.ivCreator)
        var tvCreatorName = itemView.findViewById<TextView>(R.id.tvCreatorName)
    }
}