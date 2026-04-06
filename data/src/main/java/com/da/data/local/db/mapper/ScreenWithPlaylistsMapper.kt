package com.da.data.local.db.mapper

import com.da.data.local.db.entity.ScreenWithPlaylists
import com.da.domain.model.Playlist
import com.da.domain.model.PlaylistItem
import com.da.domain.model.Screen

class ScreenWithPlaylistsMapper {

    fun map(from: ScreenWithPlaylists): Screen {
        return Screen(
            screenKey = from.screen.screenKey,
            modified = from.screen.modified ?: 0L,
            isDownload = from.screen.isDownloaded,
            playlists = from.playlists.map { playlistWithItems ->
                Playlist(
                    playlistKey = playlistWithItems.playlist.playlistKey,
                    isDownload = playlistWithItems.playlist.isDownloaded,
                    items = playlistWithItems.items.map { item ->
                        PlaylistItem(
                            duration = item.duration ?: 0,
                            dataSize = item.dataSize ?: 0,
                            modified = item.modified ?: 0L,
                            creativeLabel = item.creativeLabel.orEmpty(),
                            playlistKey = item.playlistKey,
                            creativeKey = item.creativeKey.orEmpty(),
                            orderKey = item.orderKey ?: 0
                        )
                    }
                )
            }
        )
    }
}