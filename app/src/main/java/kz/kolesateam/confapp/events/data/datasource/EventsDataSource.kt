package kz.kolesateam.confapp.events.data.datasource

import kz.kolesateam.confapp.events.domain.models.BranchApiData
import kz.kolesateam.confapp.events.domain.models.EventApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventsDataSource {

    @GET("/upcoming_events")
    fun getUpcomingEvents(): Call<List<BranchApiData>>

    @GET("/branch_events/{id}")
    fun getBranchEvents(@Path("id") id: Int): Call<List<EventApiData>>

    @GET("/events/{id}")
    fun getEventDetails(@Path("id") id: Int): Call<EventApiData>
}