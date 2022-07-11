package kz.kolesateam.confapp.events.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.BranchApiData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable

class BranchAdapter(
    private val branchClickListener: BranchClickListener,
    private val favoriteClickListener: FavoriteClickListener,
    private val eventClickListener: EventClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val dataList: MutableList<UpcomingEventListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            1 -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_layout, parent,false))
            else -> BranchViewHolder(
                View.inflate(parent.context, R.layout.branch_item, null),
                branchClickListener,
                favoriteClickListener,
                eventClickListener,
                favoriteEventActionObservable
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.onBind(dataList[position].data as String)
        }
        if (holder is BranchViewHolder) {
            holder.onBind(dataList[position].data as BranchApiData)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as? BranchViewHolder)?.onViewRecycled()

    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type
    }

    fun setList(branchApiDataList: List<UpcomingEventListItem>) {
        dataList.clear()
        dataList.addAll(branchApiDataList)
        notifyDataSetChanged()
    }
}

