package kz.kolesateam.confapp.events.presentation.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val userNameTextView: TextView = itemView.findViewById(R.id.header_text_view_username)

    fun onBind(userName: String) {
        userNameTextView.text = userName
    }
}