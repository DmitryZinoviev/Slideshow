package com.da.data.local.db.mapper

import com.da.data.local.db.entity.PlaylistEntity
import com.da.data.local.db.entity.PlaylistItemEntity
import com.da.data.local.db.entity.ScreenEntity
import com.da.domain.model.Screen

class ScreenEntityMapper(
    private val playlistMapper: PlaylistEntityMapper
) {

    fun map(
        screen: ScreenEntity,
        playlists: List<PlaylistEntity>,
        playlistItems: List<PlaylistItemEntity>
    ): Screen {

        val itemsGrouped = playlistItems.groupBy { it.playlistKey }

        val mappedPlaylists = playlists.map { playlist ->
            val items = itemsGrouped[playlist.playlistKey].orEmpty()
            playlistMapper.map(playlist, items)
        }

        return Screen(
            screenKey = screen.screenKey,
            playlists = mappedPlaylists,
            modified = screen.modified ?: 0L
        )
    }
}