package com.tntra.pargo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tntra.pargo.R
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.comments.CommentListModel
import com.tntra.pargo.model.comments.list.Comment
import java.lang.Exception

class ContentMessageAdapter(var list: ArrayList<Comment>,
                            var context: Context,
                            var onpositionClick: onClickAdapter?) : RecyclerView.Adapter<ContentMessageAdapter.Viewholder>() {
    private val positionClick: onClickAdapter = onpositionClick!!
    val MESSAGE_TYPE_IN = 1
    val MESSAGE_TYPE_OUT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
//        Log.e("TAGG", "onCreateViewHolder: " + viewType)
//        if (viewType == MESSAGE_TYPE_IN) {
        return Viewholder(LayoutInflater.from(context).inflate(R.layout.comment_adapter_design, parent, false));
//        }
//        return OutViewholder(LayoutInflater.from(context).inflate(R.layout.comment_adapter_design, parent, false));
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.tvComment?.text = list[position].attributes.message
        try {
            Glide.with(context).load(Common.url + list[position].attributes.user_profile_img_path).into(holder.ivmessageImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvComment = itemView.findViewById<TextView>(R.id.tvComment)
        var ivmessageImage = itemView.findViewById<ImageView>(R.id.ivmessageImage)
    }

    class OutViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}