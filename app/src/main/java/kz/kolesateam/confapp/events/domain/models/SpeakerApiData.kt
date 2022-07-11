package kz.kolesateam.confapp.events.domain.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SpeakerApiData(
        @JsonProperty("id")
        val id: Int,
        @JsonProperty("fullName")
        val fullName: String?,
        @JsonProperty("job")
        val job: String?,
        @JsonProperty("photoUrl")
        val photoUrl: String?,
        @JsonProperty("isInvited")
        val isInvited: Boolean?
)