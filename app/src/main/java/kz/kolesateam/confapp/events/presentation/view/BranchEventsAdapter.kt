package kz.kolesateam.confapp.events.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.domain.models.UpcomingEventListItem
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventActionObservable

class BranchEventsAdapter(
    private val favoriteClickListener: FavoriteClickListener,
    private val eventClickListener: EventClickListener,
    private val favoriteEventActionObservable: FavoriteEventActionObservable
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<UpcomingEventListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            1 -> BranchTitleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.activity_all_events_header_layout, parent,false))
            else -> BranchEventsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.branch_events_list_item, parent,false),
                favoriteClickListener,
                eventClickListener,
                favoriteEventActionObservable
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BranchTitleViewHolder) {
            holder.onBind(dataList[position].data as String)
        }
        if (holder is BranchEventsViewHolder) {
            holder.onBind(dataList[position].data as EventApiData)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as? BranchEventsViewHolder)?.onViewRecycled()
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(branchEventDataList: List<UpcomingEventListItem>) {
        dataList.clear()
        dataList.addAll(branchEventDataList)
        notifyDataSetChanged()
    }
}
