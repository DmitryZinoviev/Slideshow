package com.da.slideshow.ui.main.component

import android.media.browse.MediaBrowser
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.core.net.toUri

@Composable
fun VideoPlayer(path: String) {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build()
    }


    LaunchedEffect(path) {
        val mediaItem = MediaItem.fromUri(path.toUri())
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                this.player = player
            }
        }
    )
}