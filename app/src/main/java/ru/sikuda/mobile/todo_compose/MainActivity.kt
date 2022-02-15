package ru.sikuda.mobile.todo_compose

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.sikuda.mobile.todo_compose.model.Note
import ru.sikuda.mobile.todo_compose.ui.theme.Todo_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Todo_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyApp {
                        startActivity(DetailActivity.newIntent(this, it))
                    }
                }
            }
        }
    }
}

sealed class NavScreen(val route: String) {
    object list: NavScreen("list")
    object detail: NavScreen("detail")
}

val items = listOf(
    NavScreen.list,
    NavScreen.detail,
)

@Composable
fun MyApp(navigateToProfile: (Note) -> Unit) {

//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "profile") {
//        composable("list") {
//            composable("list"){ Conversation(messages = , navigateToProfile = ) }
//            composable("detail") { Detail(/*...*/) }
//        /*...*/
//    }

    Scaffold(
        content = {
             Conversation(SampleData.conversationSample, navigateToProfile)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Todo_composeTheme {
        MyApp {
            //startActivity(ProfileActivity.newIntent(this, it))
        }
    }
}

@Composable
fun Conversation(messages: List<Note>, navigateToProfile:(Note) -> Unit ) {

    //val result = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Notes with foto")
                },

                navigationIcon = {
                    // show drawer icon
                    IconButton(
                        onClick = {
                            showToast( context, "Not implemented")
                            //Toast.makeText( context,"Showing toast....", Toast.LENGTH_SHORT).show()
                            //result.value = "Not implemented"
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = "")
                    }
                },

                actions = {

                    Box(
                        Modifier.wrapContentSize(Alignment.TopEnd)
                    ) {
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                expanded.value = false
                                //result.value = "First item clicked"
                            }) {
                                Text("First Item")
                            }

                            Divider()

                            DropdownMenuItem(onClick = {
                                expanded.value = false
                                //result.value = "Second item clicked"
                            }) {
                                Text("Second item")
                            }
                        }
                    }
                },

                //backgroundColor = Color(0xFDCD7F32),
                elevation = AppBarDefaults.TopAppBarElevation
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val msg = Note(0, "", "", "","")
                    navController.navigate(0)
                    //navController.popBackStack()
                    //navigateToProfile(msg)
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )
        },

        content = {
            LazyColumn {
                items(messages) { message ->
                    MessageCard(message, navigateToProfile )
                }
            }
        }
    )
}

@Composable
fun MessageCard(msg: Note, navigateToProfile: (Note) -> Unit,) {
    val navController = rememberNavController()

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.clickable {
            //navigateToProfile(msg)
            //navController.navigate(NavDirections())
        }) {
            Text(
                text = msg.content,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = MaterialTheme.colors.surface
            ) {
                Text(
                    text = msg.details,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = Int.MAX_VALUE,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun DetailScreen(message: Note) {
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
        navController.popBackStack()
    }) {
        Text("Back")

    }
}

fun showToast(context: Context, str: String) {
    Toast.makeText( context, str, Toast.LENGTH_SHORT).show()
}