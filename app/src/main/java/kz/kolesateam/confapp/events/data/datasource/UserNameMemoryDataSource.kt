package kz.kolesateam.confapp.events.data.datasource

private const val DEFAULT_USER_NAME = "Guest"

class UserNameMemoryDataSource: UserNameDataSource {

    private var username: String? = null

    override fun getSavedUserName(): String = username ?: DEFAULT_USER_NAME

    override fun saveUserName(username: String) {
        this.username = username
    }
}