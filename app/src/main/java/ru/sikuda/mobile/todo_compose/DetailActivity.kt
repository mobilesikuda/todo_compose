package ru.sikuda.mobile.todo_compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import ru.sikuda.mobile.todo_compose.model.Note
import ru.sikuda.mobile.todo_compose.ui.theme.Todo_composeTheme


class DetailActivity: ComponentActivity() {

    private val message: Note by lazy {
        intent?.getSerializableExtra(MESSAGE_ID) as Note
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Todo_composeTheme {
                ProfileScreen(message)
            }
        }
    }

    companion object {
        private const val MESSAGE_ID = "MESSAGE_id"
        fun newIntent(context: Context, message: Note) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(MESSAGE_ID, message)
            }
    }
}

@Composable
fun ProfileScreen(message: Note, onNavIconPressed: () -> Unit = { }) {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    Divider()
                    Text(message.date)
                    Divider()
                    Text(message.content)
                    Divider()
                    Text(message.details)
                    Divider()

                }
            }
        }
    }
    Button(onClick = {
        //TODO
    }) {
    Text("Back")

    }
}