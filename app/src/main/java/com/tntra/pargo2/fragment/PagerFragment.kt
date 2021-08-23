package com.tntra.pargo2.fragment

import android.R.attr.data
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.tntra.pargo2.R
import com.tntra.pargo2.activities.ContentDetailActivity
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.model.treading_content.Content
import com.tntra.pargo2.model.treading_content.TreadingContentModel
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PagerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var tvCreatorName: TextView? = null
    private var tvDescription: TextView? = null
    private var ivCoverLatestContent: ImageView? = null
    private var tvPassion: TextView? = null
    private var tvFileTime: TextView? = null
    private var videoContent: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.e("TAG", "onCreate: " + param2)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pager, container, false)
        Log.e("TAGG", "onCreateView: pagerfragment")
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        videoContent = view?.findViewById(R.id.videoContent)
        tvCreatorName = view?.findViewById(R.id.tvCreatorName)
        tvFileTime = view?.findViewById(R.id.tvFileTime)
        tvPassion = view?.findViewById(R.id.tvPassion)
        tvDescription = view?.findViewById(R.id.tvDescription)
        ivCoverLatestContent = view?.findViewById(R.id.ivCoverLatestContent)

        val gson = Gson()
        val model = gson.fromJson(param2, Content::class.java)

        tvCreatorName?.text = model.attributes.name

        try {
            Log.e("TAG", "initView: " + Common.url + model.attributes.cover_img_path)
            context?.let { Glide.with(it).load(Common.url + model.attributes.cover_img_path).into(ivCoverLatestContent!!) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tvDescription?.text = model.attributes.body
        tvPassion?.text = model.attributes.genre?.name
        tvFileTime?.text = model.attributes.duration

        videoContent?.setOnClickListener {
            val intent = Intent(context, ContentDetailActivity::class.java)
            intent.putExtra("video_link", model.attributes.posts_path)
            intent.putExtra("id", model?.id)
            context?.startActivity(intent)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PagerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAGG", "onResume: pagerfragment")
    }
}