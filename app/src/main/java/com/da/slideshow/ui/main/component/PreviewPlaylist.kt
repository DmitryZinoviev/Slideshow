package com.da.slideshow.ui.main.component

import android.view.SurfaceView
import android.view.TextureView
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import coil.compose.rememberAsyncImagePainter
import com.da.domain.model.PlaylistItemForReplay
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun PreviewPlaylist(
    items: List<PlaylistItemForReplay>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                "No preview available",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        return
    }
    var job by remember { mutableStateOf<Job?>(null) }
    val context = LocalContext.current
    val crossfadeMs = 1000L
    var currentIndex by remember { mutableIntStateOf(0) }
    var nextIndex by remember { mutableIntStateOf(if (items.size > 1) 1 else 0) }

    var alphaCurrent by remember { mutableFloatStateOf(1f) }
    var alphaNext by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(true) }

    val player = remember {
        androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
            repeatMode = androidx.media3.common.Player.REPEAT_MODE_ALL
        }
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {
                    isPlaying = true
                    player.play()


                }, enabled = !isPlaying) {
                    Text("Start")
                }
                Button(onClick = {
                    isPlaying = false;
                    player.pause()

                }, enabled = isPlaying) {
                    Text("Stop")
                }
                Button(onClick = {
                    job?.cancel()
                    currentIndex = nextIndex
                    nextIndex = (nextIndex + 1) % items.size

                    alphaCurrent = 1f
                    alphaNext = 0f
                }, enabled = isPlaying) {
                    Text("Skip")
                }
            }

            val currentItem = items[currentIndex]
            if (currentItem.isVideo) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alphaCurrent),
                    factory = { ctx ->
                        val view = androidx.media3.ui.PlayerView(ctx).apply {
                            this.player = player
                            useController = false
                        }
                        view.videoSurfaceView?.let { surface ->
                            if (surface is SurfaceView) {
                                val textureView = TextureView(ctx)
                                val parent = surface.parent as ViewGroup
                                val index = parent.indexOfChild(surface)
                                parent.removeView(surface)
                                parent.addView(textureView, index, surface.layoutParams)
                                player.setVideoTextureView(textureView)
                            }
                        }
                        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
                        player.setMediaItem(androidx.media3.common.MediaItem.fromUri(currentItem.path.toUri()))
                        player.prepare()
                        player.playWhenReady = true
                        view
                    }
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(currentItem.path),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alphaCurrent),
                    contentScale = ContentScale.Crop
                )
            }

            if (items.size > 1) {
                val nextItem = items[nextIndex]
                if (nextItem.isVideo) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(alphaNext),
                        factory = { ctx ->
                            val view = androidx.media3.ui.PlayerView(ctx).apply {
                                this.player = player
                                useController = false
                            }
                            view.videoSurfaceView?.let { surface ->
                                if (surface is SurfaceView) {
                                    val textureView = TextureView(ctx)
                                    val parent = surface.parent as ViewGroup
                                    val index = parent.indexOfChild(surface)
                                    parent.removeView(surface)
                                    parent.addView(textureView, index, surface.layoutParams)
                                    player.setVideoTextureView(textureView)
                                }
                            }
                            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
                            player.setMediaItem(androidx.media3.common.MediaItem.fromUri(nextItem.path.toUri()))
                            player.prepare()
                            player.playWhenReady = true
                            view
                        }
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(items[nextIndex].path),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(alphaNext),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        if (items.size > 1) {
            LaunchedEffect(currentIndex, isPlaying) {


                if (!isPlaying) {
                    job?.cancel()
                    return@LaunchedEffect
                }
                job = launch {
                    val duration = items[currentIndex].durationSec * 1000
                    val showTime = (duration - crossfadeMs).coerceAtLeast(0)

                    delay(showTime)

                    val steps = 20
                    repeat(steps) { i ->
                        val fraction = (i + 1) / steps.toFloat()
                        alphaCurrent = 1f - fraction
                        alphaNext = fraction
                        delay(crossfadeMs / steps)
                    }

                    currentIndex = nextIndex
                    nextIndex = (nextIndex + 1) % items.size

                    alphaCurrent = 1f
                    alphaNext = 0f
                }
            }

        }
    }
}