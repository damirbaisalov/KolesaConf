package kz.kolesateam.confapp.hello.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.di.SHARED_PREFS_DATA_SOURCE
import kz.kolesateam.confapp.events.data.datasource.UserNameDataSource
import kz.kolesateam.confapp.events.presentation.UpcomingEventsActivity
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class HelloActivity : AppCompatActivity() {

    private val usernameDataSource: UserNameDataSource by inject(named(SHARED_PREFS_DATA_SOURCE))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        val inputNameEditText: EditText = findViewById(R.id.activity_hello_name_edit_text)
        val continueButton: Button = findViewById(R.id.activity_hello_continue_button)

        continueButton.setOnClickListener{
            val name = inputNameEditText.text.toString()
            saveUserName(name)
            navigateToUpcomingEventsScreen()
        }

        inputNameEditText.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val isInputEmpty: Boolean = s.toString().isBlank()
                continueButton.isEnabled = !isInputEmpty
            }
        })
    }

    private fun navigateToUpcomingEventsScreen() {
        val upcomingEventsScreenIntent = Intent (this, UpcomingEventsActivity::class.java)
        startActivity(upcomingEventsScreenIntent)
    }

    private fun saveUserName(userName: String){
        usernameDataSource.saveUserName(userName)
    }
}