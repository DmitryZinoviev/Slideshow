package com.da.data.local.db.mapper

import com.da.data.local.db.entity.PlaylistItemEntity
import com.da.domain.model.PlaylistItem

class PlaylistItemEntityMapper {

    fun map(from: PlaylistItemEntity): PlaylistItem {
        return PlaylistItem(
            duration = from.duration ?: 0,
            dataSize = from.dataSize ?: 0,
            modified = from.modified ?: 0L,
            creativeLabel = from.creativeLabel.orEmpty(),
            playlistKey = from.playlistKey,
            creativeKey = from.creativeKey.orEmpty(),
            orderKey = from.orderKey ?: 0
        )
    }
}