package com.tntra.pargo.adapter

import android.R.attr.path
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tntra.pargo.R
import com.tntra.pargo.activities.ContentDetailActivity
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.treading_content.Content
import java.io.File
import java.net.URI


class TreadingContentAdapter(var item: ArrayList<Content>?,
                             var context: Context,
                             var onPositionClick: onClickAdapter?

) : RecyclerView.Adapter<TreadingContentAdapter.ViewHolder>() {
    private val positionClick: onClickAdapter = onPositionClick!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recommanded_content_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        if (position == 0) {
//            val params = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            params.setMargins(0, 0, 0, 0)
//            holder.llEventContent.setLayoutParams(params)
//        } else {
//            val params = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            params.setMargins(0, 35, 0, 0)
//            holder.llEventContent.setLayoutParams(params)
//
//        }

        holder.videoContent.setOnClickListener {
            Log.e("TAGG", "onBindViewHolder: "+item?.get(position)?.attributes?.posts_path )
            Log.e("TAGG", "onBindViewHolder: "+item?.get(position)?.attributes?.body )
            val intent = Intent(context, ContentDetailActivity::class.java)
//            Log.e("TAGclick", "onPositionClick: "+treadingContentList?.get(check)?.attributes?.posts_path )
            intent.putExtra("video_link", item?.get(position)?.attributes?.posts_path)
//            intent.putExtra("video_link","https://pargo-back-end-devlopment.s3.ap-south-1.amazonaws.com//storage/emulated/0/20210727_223815.mp41628514097792")
//            intent.putExtra("video_link", "https://pargo-back-end-devlopment.s3.ap-south-1.amazonaws.com/iOS/trim.3D4A8881-D584-4E6C-B3F2-C2FE10E89125.MOV")
//            intent.putExtra("video_link", "https://pargo-back-end-devlopment.s3.ap-south-1.amazonaws.com/ganpati+sanskrit/AnyConv.com__Hanuman+Sankirtan.m3u8")
//            intent.putExtra("video_link", "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
//            intent.putExtra("video_link","https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3")
            context.startActivity(intent)
        }

        holder.tvTitle.text = item?.get(position)?.attributes?.body
        holder.tvName.text = item?.get(position)?.attributes?.name

        try {
            Glide.with(context).load(Common.url + item?.get(position)?.attributes?.user_profile_img_path)
                    .into(holder.ivImg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            Glide.with(context).load(Common.url + item?.get(position)?.attributes?.cover_img_path)
                    .into(holder.ivCover)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.tvFileTime.text = item?.get(position)?.attributes?.duration
        holder.tvDate.text = item?.get(position)?.attributes?.created_at
    }

    override fun getItemCount(): Int {
        return item?.size!!
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var llEventContent = itemView.findViewById(R.id.llContent) as LinearLayout
        var videoContent = itemView.findViewById(R.id.videoContent) as CardView
        var tvName = itemView.findViewById(R.id.tvName) as TextView
        var ivImg = itemView.findViewById(R.id.ivImg) as ImageView
        var tvTitle = itemView.findViewById(R.id.tvTitle) as TextView
        var ivCover = itemView.findViewById(R.id.ivCover) as ImageView
        var tvDate = itemView.findViewById(R.id.tvDate) as TextView
        var tvFileTime = itemView.findViewById(R.id.tvFileTime) as TextView

    }
}