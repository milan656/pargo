package com.tntra.pargo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo.R
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.followers.FollowerModel

class FollowersListAdapter(var list: ArrayList<FollowerModel>,
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

        holder.tvFollowerName.text = list[position].name
        holder.tvpassion.text = list[position].passion

        holder.btnSelect.setOnClickListener {

            list[position].isChecked = !list[position].isChecked
            holder.btnSelect.isChecked = list[position].isChecked

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var view = itemView.findViewById(R.id.view) as View
        var tvFollowerName = itemView.findViewById(R.id.tvFollowerName) as TextView
        var tvpassion = itemView.findViewById(R.id.tvpassion) as TextView
        var btnSelect = itemView.findViewById(R.id.btnSelect) as CheckBox

    }

    fun getall(): ArrayList<FollowerModel>? {
        return list
    }

    fun getSelected(): ArrayList<FollowerModel>? {
        val selected: ArrayList<FollowerModel> = ArrayList()
        for (i in 0 until list.size) {
            if (list.get(i).isChecked) {
                selected.add(list.get(i))
            }
        }
        return selected
    }
}