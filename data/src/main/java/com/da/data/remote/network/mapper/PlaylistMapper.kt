package com.da.data.remote.network.mapper

import com.da.data.remote.network.dto.PlaylistsResponse
import com.da.domain.model.Playlist

class PlaylistMapper(
    private val itemMapper: PlaylistItemMapper
) {

    fun map(from: PlaylistsResponse): Playlist {
        return Playlist(
            playlistKey = from.playlistKey.orEmpty(),
            items = from.playlistItems?.filter { it?.playlistKey == from.playlistKey.orEmpty()}
                ?.mapNotNull { it?.let(itemMapper::map) }
                .orEmpty()
        )
    }
}