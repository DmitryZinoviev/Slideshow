package com.da.data.local.db.entity

import com.da.data.remote.network.dto.ScreenResponse

fun ScreenResponse.toEntities(): Triple<ScreenEntity, List<PlaylistEntity>, List<PlaylistItemEntity>> {
    val screen = ScreenEntity(
        screenKey = screenKey ?: "",
        modified = modified
    )

    val playlists = mutableListOf<PlaylistEntity>()
    val items = mutableListOf<PlaylistItemEntity>()

    this.playlists.forEach { playlistResponse ->
        val playlistKey = playlistResponse?.playlistKey ?: return@forEach

        val playlistEntity = PlaylistEntity(
            playlistKey = playlistKey,
            screenKey = screen.screenKey
        )
        playlists.add(playlistEntity)

        playlistResponse.playlistItems.forEach { item ->
            if (item != null) {
                items.add(
                    PlaylistItemEntity(
                        duration = item.duration,
                        dataSize = item.dataSize,
                        modified = item.modified,
                        creativeLabel = item.creativeLabel,
                        playlistKey = playlistKey,
                        creativeKey = item.creativeKey,
                        orderKey = item.orderKey
                    )
                )
            }
        }
    }

    return Triple(screen, playlists, items)
}