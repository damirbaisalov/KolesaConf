package kz.kolesateam.confapp.favorite_events.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.domain.models.EventApiData
import kz.kolesateam.confapp.events.presentation.view.EventClickListener

class FavoriteEventsAdapter(
    private val eventClickListener: EventClickListener
): RecyclerView.Adapter<FavoriteEventsViewHolder>() {

    private val dataList: MutableList<EventApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventsViewHolder {
        return FavoriteEventsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.branch_events_list_item, parent,false),
            eventClickListener
        )
    }

    override fun onBindViewHolder(holder: FavoriteEventsViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(favoriteEventsList: List<EventApiData>) {
        dataList.clear()
        dataList.addAll(favoriteEventsList)
        notifyDataSetChanged()
    }
}