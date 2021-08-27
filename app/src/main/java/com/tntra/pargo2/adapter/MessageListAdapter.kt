package com.tntra.pargo2.adapter

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tntra.pargo2.R
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.common.OnBottomReachedListener
import com.tntra.pargo2.common.onClickAdapter
import com.tntra.pargo2.model.FollowerModel
import com.tntra.pargo2.model.collabRoomList.CollabRoom
import de.hdodenhof.circleimageview.CircleImageView

class MessageListAdapter(var list: ArrayList<CollabRoom>,
                         var context: Context,
                         var onpositionClick: onClickAdapter?,
                         var onBottomReachedListener: OnBottomReachedListener
) : RecyclerView.Adapter<MessageListAdapter.Viewholder>() {

    private val positionClick: onClickAdapter = onpositionClick!!
    private val onbottom: OnBottomReachedListener = onBottomReachedListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_adapter_design, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        if (position == list.size - 1) {
            holder.view.visibility = View.GONE
        } else {
            holder.view.visibility = View.VISIBLE
        }

        holder.tvname.text = list[position].attributes.name
        if (list[position].attributes.collab_room_type != null) {
            holder.tvCollabRoomDesc.text = list[position].attributes.collab_room_type
        } else {
            holder.tvCollabRoomDesc.text = ""
        }

        holder.llcontent.setOnClickListener {

            onpositionClick?.onPositionClick(position, 5)
        }

        if (list.size - 1 == position) {
            onBottomReachedListener.onBottomReached(position)
        }

        try {
//            val width = 80
//            val height = 80
            val first: String = list[position].attributes.name[0].toString().capitalize()
//            val bitmap = Common.writeOnDrawable(context, first, width, height)
//            val d: Drawable = BitmapDrawable(context.getResources(), bitmap)

//            holder.ivCollabImg.setImageDrawable()

            holder.tvGroupName.text = first
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var view = itemView.findViewById(R.id.view) as View
        var tvname = itemView.findViewById(R.id.tvname) as TextView
        var tvCollabRoomDesc = itemView.findViewById(R.id.tvCollabRoomDesc) as TextView
        var btnSelect = itemView.findViewById(R.id.btnSelect) as TextView
        var llcontent = itemView.findViewById(R.id.llcontent) as LinearLayout
        var ivCollabImg = itemView.findViewById(R.id.ivCollabImg) as CircleImageView
        var tvGroupName = itemView.findViewById(R.id.tvGroupName) as TextView

    }

//    fun getall(): ArrayList<FollowerModel>? {
//        return list
//    }
//
//    fun getSelected(): ArrayList<FollowerModel>? {
//        val selected: ArrayList<FollowerModel> = ArrayList()
//        for (i in 0 until list.size) {
//            if (list.get(i).isChecked) {
//                selected.add(list.get(i))
//            }
//        }
//        return selected
//    }
}