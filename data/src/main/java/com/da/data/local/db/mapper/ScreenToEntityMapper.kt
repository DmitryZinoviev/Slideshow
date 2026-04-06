package com.da.data.local.db.mapper

import com.da.data.local.db.entity.PlaylistEntity
import com.da.data.local.db.entity.PlaylistItemEntity
import com.da.data.local.db.entity.ScreenEntity
import com.da.domain.model.Screen

class ScreenToEntityMapper{

    fun map(from: Screen): Triple<ScreenEntity, List<PlaylistEntity>, List<PlaylistItemEntity>> {

        val screenEntity = ScreenEntity(
            screenKey = from.screenKey,
            modified = from.modified,
            isDownloaded = from.isDownload
        )

        val playlists = from.playlists.map { playlist ->
            PlaylistEntity(
                playlistKey = playlist.playlistKey,
                screenKey = from.screenKey,
                isDownloaded = playlist.isDownload
            )
        }

        val items = from.playlists.flatMap { playlist ->
            playlist.items.map { item ->
                PlaylistItemEntity(
                    duration = item.duration,
                    dataSize = item.dataSize,
                    modified = item.modified,
                    creativeLabel = item.creativeLabel,
                    playlistKey = playlist.playlistKey,
                    creativeKey = item.creativeKey,
                    orderKey = item.orderKey
                )
            }
        }

        return Triple(screenEntity, playlists, items)
    }
}