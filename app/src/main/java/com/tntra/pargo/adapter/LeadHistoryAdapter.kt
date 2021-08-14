package com.tntra.pargo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter
import com.tntra.pargo.R
import com.tntra.pargo.common.OnBottomReachedListener
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.DashboardModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class LeadHistoryAdapter(
        private val mActivity: Context, dataset: List<DashboardModel>,
        onPositionClick: onClickAdapter
) :
        RecyclerView.Adapter<LeadHistoryAdapter.ViewHolder>(),
        StickyHeaderAdapter<LeadHistoryAdapter.HeaderHolder?> {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private val mDataset: List<DashboardModel>
    private val mDateFormat: SimpleDateFormat
    private val mDateFormatTime: SimpleDateFormat
    private var mToday = ""
    var onclick: onClickAdapter? = null
    private var onBottomReachedListener: OnBottomReachedListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.design_home_adapter_item, parent, false)
        return ViewHolder(view)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener?) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e("getdatee00", "" + mDataset.get(position))
        val item: DashboardModel = mDataset[position]


        holder.tvMessage?.text = mDataset.get(position).fullAddress
        holder.tvgroupName?.text = mDataset.get(position).addressTitle

        if (mDataset.get(position).requestAccepted) {
            holder.llfooter?.visibility = View.GONE
        } else {
            holder.llfooter?.visibility = View.VISIBLE
        }

        holder.tvRejectReq?.setOnClickListener {
            onclick?.onPositionClick(position, 1)
        }
        holder.tvAcceptReq?.setOnClickListener {
            onclick?.onPositionClick(position, 0)
        }
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun getHeaderId(position: Int): Long {
        var headerId = 0L
        try {
            if (mDataset != null && mDataset.size > 0) {
                val item: DashboardModel = mDataset[position]
                headerId = mDateFormat.parse(mDateFormat.format(Date(item.createdAt))).time
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return headerId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderHolder {
        val view: View = mInflater.inflate(R.layout.header_lead, parent, false)
        return HeaderHolder(view)
    }


    class ViewHolder(mRoot: View) : RecyclerView.ViewHolder(
            mRoot
    ) {

        var tvgroupName = mRoot.findViewById<TextView>(R.id.tvgroupName)
        var tvMessage = mRoot.findViewById<TextView>(R.id.tvMessage)
        var tvAcceptReq = mRoot.findViewById<TextView>(R.id.tvAcceptReq)
        var tvRejectReq = mRoot.findViewById<TextView>(R.id.tvRejectReq)
        var llfooter = mRoot.findViewById<LinearLayout>(R.id.llfooter)

    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timestamp = itemView.findViewById<TextView>(R.id.timestamp)
    }


    init {
        //mContext = context;
        mContext = mActivity
        mInflater = LayoutInflater.from(mContext)
        mDataset = dataset
        mDateFormat = SimpleDateFormat("dd MMMM yy")
        mDateFormatTime = SimpleDateFormat("hh:mm a")
        mToday = mDateFormat.format(Date())
    }

    override fun onBindHeaderViewHolder(p0: HeaderHolder?, p1: Int) {
        val item: DashboardModel = mDataset[p1]

        p0?.timestamp?.text = mDateFormat.format(Date(item.createdAt)).toString()
        Log.e("gettimedate", "" + mDataset.get(p1))
        if (mToday == p0?.timestamp?.text) {
            p0.timestamp?.text = "Today"
        }
    }
}
