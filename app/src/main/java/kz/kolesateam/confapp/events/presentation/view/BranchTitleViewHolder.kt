package kz.kolesateam.confapp.events.presentation.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R

class BranchTitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val branchTitleTextView : TextView = itemView.findViewById(R.id.activity_all_events_branch_title)

    fun onBind(branchTitle: String) {
        branchTitleTextView.text = branchTitle
    }
}