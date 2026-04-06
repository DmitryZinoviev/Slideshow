package com.da.slideshow.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.da.domain.model.PlaylistItemForReplay
import com.da.slideshow.ui.main.component.PreviewPlaylist
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(modifier: Modifier, viewModel: MainViewModel = koinViewModel()) {
    var screenKey by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()
    MainView(
        modifier, state.screenKey, state.blockScreenKeyUpdate, state.list,
        {
            viewModel.onAction(action = MainAction.ChangeScreenKeyAction(it))
        },
        {

        }) {
        viewModel.onAction(MainAction.FetchScreenAction(it))

    }

}

@Composable
fun MainView(
    modifier: Modifier,
    screenKey: String,
    blockScreenKeyUpdate: Boolean,
    list: List<PlaylistItemForReplay>,
    onScreenKeyChange: (String) -> Unit,
    onGetDbClick: () -> Unit,
    onSyncClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (blockScreenKeyUpdate) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        TextField(
            value = screenKey,
            onValueChange = onScreenKeyChange,
            placeholder = { Text("ScreenKey") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !blockScreenKeyUpdate
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSyncClick(screenKey) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !blockScreenKeyUpdate
        ) {
            Text("Sync")
        }

        Spacer(modifier = Modifier.height(16.dp))


        PreviewPlaylist(list)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainView(Modifier,"", true, emptyList(), {}, {}){


    }
}