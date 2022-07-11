package kz.kolesateam.confapp.events.presentation.view

import kz.kolesateam.confapp.events.domain.models.EventApiData

interface BranchClickListener {

    fun onBranchClick(branchId: Int, branchTitle: String)

//    fun onEventClick(
//            event: EventApiData
//    )
}