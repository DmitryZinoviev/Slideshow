package com.da.data.local.db.mapper

import com.da.data.local.db.entity.PlaylistEntity
import com.da.data.local.db.entity.PlaylistItemEntity
import com.da.domain.model.Playlist

class PlaylistEntityMapper(
    private val itemMapper: PlaylistItemEntityMapper
) {

    fun map(
        playlist: PlaylistEntity,
        items: List<PlaylistItemEntity>
    ): Playlist {
        return Playlist(
            playlistKey = playlist.playlistKey,
            items = items.map(itemMapper::map)
        )
    }
}