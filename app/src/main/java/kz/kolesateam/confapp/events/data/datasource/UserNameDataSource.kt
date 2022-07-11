package kz.kolesateam.confapp.events.data.datasource

interface UserNameDataSource {

    fun getSavedUserName(): String

    fun saveUserName(
        username: String
    )
}