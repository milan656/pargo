package com.tntra.pargo.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo.R
import com.tntra.pargo.activities.GenersDetailActivity
import com.tntra.pargo.adapter.EventAdapter
import com.tntra.pargo.adapter.ExploreGeneresAdapter
import com.tntra.pargo.adapter.ExploreGeneresDetailAdapter
import com.tntra.pargo.adapter.RecommandCreatorsAdapter
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.creatorListsearch.CreatorsListMdel
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment(), onClickAdapter {
    private var param1: String? = null
    private var param2: String? = null
    var exploreGeneresRecycView: RecyclerView? = null
    var eventAdapter: EventAdapter? = null
    private var timer: Timer? = null

    var exploreGenList: ArrayList<String>? = ArrayList()
    var recommandCreatorsList: ArrayList<CreatorsListMdel>? = ArrayList()
    var exploreGeneresAdapter: ExploreGeneresDetailAdapter? = null
    var recommandedCreatorsRecycView: RecyclerView? = null
    var recommandCreatorsAdapter: RecommandCreatorsAdapter? = null
    private var tvNoGeners: TextView? = null
    private var tvNoCreators: TextView? = null
    private var edtsearch: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        initView(view)
        return view
    }

    private fun initView(view: View?) {
        tvNoCreators = view?.findViewById(R.id.tvNoCreators);
        edtsearch = view?.findViewById(R.id.edtsearch);
        tvNoGeners = view?.findViewById(R.id.tvNoGeners);
        exploreGeneresRecycView = view?.findViewById(R.id.exploreGeneresRecycView);
        recommandedCreatorsRecycView = view?.findViewById(R.id.recommandedCreatorsRecycView);

        exploreGenList?.add("jazz")
        exploreGenList?.add("techno")
        exploreGenList?.add("rnb")
        exploreGenList?.add("hiphop")
        exploreGenList?.add("blues")

        for (i in 1..4) {
            recommandCreatorsList?.add(CreatorsListMdel("http://placehold.it/120x120&text=image1",
                    "Creator Name " + i))
        }

        activity?.let {
            recommandCreatorsAdapter = RecommandCreatorsAdapter(recommandCreatorsList!!, it, this)
        }
        recommandedCreatorsRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recommandedCreatorsRecycView?.adapter = recommandCreatorsAdapter

        activity?.let {
            exploreGeneresAdapter = ExploreGeneresDetailAdapter(exploreGenList!!, it, this)
        }

        exploreGeneresRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        exploreGeneresRecycView?.adapter = exploreGeneresAdapter

        edtsearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                timer = Timer()
                timer?.schedule(
                        object : TimerTask() {
                            override fun run() {
                                activity?.runOnUiThread {

//                                    api call search

                                }
                            }
                        }, 600
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (timer != null) {
                    timer?.cancel()
                }
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 4) {
            val intent = Intent(context, GenersDetailActivity::class.java)
            intent.putExtra("geners", exploreGenList?.get(variable))
            startActivity(intent)
        }
    }
}