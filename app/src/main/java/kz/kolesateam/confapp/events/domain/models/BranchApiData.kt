package kz.kolesateam.confapp.events.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BranchApiData(
        @JsonProperty("id")
        val id: Int?,
        @JsonProperty("title")
        val title: String?,
        @JsonProperty("events")
        val events: List<EventApiData>?
)