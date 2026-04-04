package com.da.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistWithItems(
    @Embedded val playlist: PlaylistEntity,

    @Relation(
        parentColumn = "playlistKey",
        entityColumn = "playlistKey"
    )
    val items: List<PlaylistItemEntity>
)

data class ScreenWithPlaylists(
    @Embedded val screen: ScreenEntity,

    @Relation(
        entity = PlaylistEntity::class,
        parentColumn = "screenKey",
        entityColumn = "screenKey"
    )
    val playlists: List<PlaylistWithItems>
)