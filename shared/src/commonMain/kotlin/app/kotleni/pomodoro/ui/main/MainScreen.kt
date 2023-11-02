package app.kotleni.pomodoro.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import app.kotleni.pomodoro.ui.timer.TimerScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = remember { MainViewModel() }
        val uiState by viewModel.uiState.collectAsState()

        var isShowCreateDialog by remember { mutableStateOf(false) }

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
                items(uiState.timers, key = { it.id }) {
                    TimerItem(
                        timer = it,
                        isActive = it.id == uiState.activeTimer?.id,
                        onItemSelected = {
                            navigator.push(TimerScreen(it.id.toInt()))
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

        SideEffect {
            viewModel.loadActiveTimer()
        }

        LaunchedEffect(key1 = "main") {
            viewModel.bindToService()
            viewModel.loadTimers()
        }
    }
}