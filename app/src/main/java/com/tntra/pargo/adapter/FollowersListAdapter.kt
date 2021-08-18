package com.tntra.pargo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tntra.pargo.R
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.followers.Follow
import com.tntra.pargo.model.followers.FollowerListModel
import com.tntra.pargo.model.followers.FollowerModel
import de.hdodenhof.circleimageview.CircleImageView

class FollowersListAdapter(var list: ArrayList<Follow>,
                           var context: Context,
                           var onpositionClick: onClickAdapter?
) : RecyclerView.Adapter<FollowersListAdapter.Viewholder>() {

    private val positionClick: onClickAdapter = onpositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.followers_list_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        if (position == list.size - 1) {
            holder.view.visibility = View.GONE
        } else {
            holder.view.visibility = View.VISIBLE
        }


        holder.btnSelect.isChecked = list[position].isChecked

        holder.tvFollowerName.text = list[position].attributes.name
        holder.tvpassion.text = "Singer"

        holder.btnSelect.setOnClickListener {

            list[position].isChecked = !list[position].isChecked
            holder.btnSelect.isChecked = list[position].isChecked

        }

        try {
            Glide.with(context).load(Common.url + list.get(position).attributes.profile_img_path).into(holder.ivFollowerImg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var view = itemView.findViewById(R.id.view) as View
        var tvFollowerName = itemView.findViewById(R.id.tvFollowerName) as TextView
        var tvpassion = itemView.findViewById(R.id.tvpassion) as TextView
        var ivFollowerImg = itemView.findViewById(R.id.ivFollowerImg) as CircleImageView
        var btnSelect = itemView.findViewById(R.id.btnSelect) as CheckBox

    }


    fun getSelected(): ArrayList<Follow>? {
        val selected: ArrayList<Follow> = ArrayList()
        for (i in 0 until list.size) {
            if (list.get(i).isChecked) {
                selected.add(list.get(i))
            }
        }
        return selected
    }
}