package app.kotleni.pomodoro.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.kotleni.pomodoro.DatabaseDriverFactory
import app.kotleni.pomodoro.Timer
import app.kotleni.pomodoro.repositories.TimersRepository
import app.kotleni.pomodoro.repositories.TimersRepositoryImpl
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class MainViewModel : KoinComponent {
    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val databaseDriverFactory: DatabaseDriverFactory by inject()
    private val timersRepository: TimersRepository = TimersRepositoryImpl(databaseDriverFactory)

    private val _timers: MutableStateFlow<List<Timer>> = MutableStateFlow(listOf())
    val timers: StateFlow<List<Timer>> = _timers

    private val _activeTimer: MutableStateFlow<Timer?> = MutableStateFlow(null)
    val activeTimer: StateFlow<Timer?> = _activeTimer

    fun loadTimers() = viewModelScope.launch {
        _timers.value = withContext(Dispatchers.IO) {
            timersRepository.fetchTimers()
        }
    }

    fun createTimer(name: String, iconId: Int, workTime: Int, sbrakeTime: Int, lbrakeTime: Int) = viewModelScope.launch {
        // Debug purpose, if started with prefix 'debug: ' use seconds instead of minutes
        val isDebugSecs = name.startsWith("debug: ")

        withContext(Dispatchers.IO) {
            val timer = Timer(
                name = name.removePrefix("debug: "),
                iconId = iconId.toLong(),
                totalWorkTime = if(isDebugSecs) Random.nextLong(0, 9999) else 0,
                totalBreakTime = if(isDebugSecs) Random.nextLong(0, 999) else 0,
                workTime = (if(isDebugSecs) workTime else workTime * 60).toLong(), // in minutes
                shortBreakTime = (if(isDebugSecs) sbrakeTime else sbrakeTime * 60).toLong(), // in minutes
                longBreakTime = (if(isDebugSecs) lbrakeTime else lbrakeTime * 60).toLong(), // in minutes
                id = 0 // automatic
            )
            timersRepository.addTimer(timer)
        }
        loadTimers()
    }

    fun removeTimer(timer: Timer) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            timersRepository.removeTimer(timer)
        }
        loadTimers()
    }
}

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = remember { MainViewModel() }
        val timers by viewModel.timers.collectAsState()

        var isShowCreateDialog by remember { mutableStateOf(false) }
        val activeTimer by viewModel.activeTimer.collectAsState()

        val lazyListState = rememberLazyListState()

        Scaffold(
            modifier = Modifier,
            topBar = {
                LargeTopAppBar(title = { Text(text = "Timers") } )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { isShowCreateDialog = true }, elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                /// IndexOfBoundsException
            LazyColumn(
                state = lazyListState,
            ) {
                items(timers, key = { it.id }) {
                    TimerItem(
                        timer = it,
                        isActive = it.id == activeTimer?.id,
                        onItemSelected = {
                            //rootNavController.navigate("timer/${it.id}")
                        },
                        onItemRemoved = {
                            viewModel.removeTimer(it)
                        }
                    )
                }
                }

                if(isShowCreateDialog) {
                    CreateTimerDialog(
                        onCreate = { name, iconId, workTime, sbrakeTime, lbrakeTime ->
                            if(name.isNotEmpty() && workTime > 0 && sbrakeTime > 0 && lbrakeTime > 0) {
                                viewModel.createTimer(name, iconId, workTime, sbrakeTime, lbrakeTime)
                                isShowCreateDialog = false
                            }
                        },
                        onDismiss = { isShowCreateDialog = false }
                    )
                }
            }
        }

//        SideEffect {
//            viewModel.loadActiveTimer()
//        }

        LaunchedEffect(key1 = "main") {
           // viewModel.bindToService()
            viewModel.loadTimers()
        }
    }
}