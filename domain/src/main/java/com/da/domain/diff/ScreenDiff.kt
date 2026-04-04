package com.da.domain.diff

import com.da.domain.model.Playlist
import com.da.domain.model.PlaylistItem

data class ScreenDiff(
    val playlists: PlaylistDiffResult,
    val isModifiedChanged: Boolean
)

data class PlaylistDiffResult(
    val added: List<Playlist>,
    val removed: List<Playlist>,
    val updated: List<PlaylistItemsDiff>
)

data class PlaylistItemsDiff(
    val playlistKey: String,
    val added: List<PlaylistItem>,
    val removed: List<PlaylistItem>,
    val updated: List<PlaylistItem>
)

data class DiffResult<T>(
    val added: List<T>,
    val removed: List<T>,
    val updated: List<T>
) {
    fun isEmpty(): Boolean =
        added.isEmpty() && removed.isEmpty() && updated.isEmpty()
}