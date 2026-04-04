package com.da.data.remote.network.mapper

import com.da.data.remote.network.dto.PlaylistItemsResponse
import com.da.domain.model.PlaylistItem

class PlaylistItemMapper {

    fun map(from: PlaylistItemsResponse): PlaylistItem {
        return PlaylistItem(
            duration = from.duration ?: 0,
            dataSize = from.dataSize ?: 0,
            modified = from.modified ?: 0L,
            creativeLabel = from.creativeLabel.orEmpty(),
            playlistKey = from.playlistKey.orEmpty(),
            creativeKey = from.creativeKey.orEmpty(),
            orderKey = from.orderKey ?: 0
        )
    }
}