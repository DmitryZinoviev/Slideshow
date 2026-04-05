package com.da.slideshow.ui.main.component

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.VideoView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat

@Composable
fun BlinkingSquareAndroidView() {
    val context = LocalContext.current

    var alpha by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            // fade in
            for (i in 0..100) {
                alpha = i / 100f
                kotlinx.coroutines.delay(10)
            }
            // fade out
            for (i in 100 downTo 0) {
                alpha = i / 100f
                kotlinx.coroutines.delay(10)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx: Context ->
                VideoView(ctx).apply {
                    setVideoURI(Uri.parse("/data/user/0/com.da.slideshow/files/media/606825e3-4f3f-489a-b9c9-1413cfa4e947.mp4"))
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = true // зациклюємо відео
                        start()
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                ViewCompat.setAlpha(view, alpha)
            }
        )
    }
}