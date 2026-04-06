package com.da.domain.model

data class Screen(
    val screenKey: String,
    val playlists: List<Playlist>,
    val modified: Long,
    val isDownload: Boolean
) {
    fun getAllPlaylistItems(): List<PlaylistItem> {
        return playlists.flatMap { it.items }.map { it }
    }
}

data class Playlist(
    val playlistKey: String,
    val items: List<PlaylistItem>,
    val isDownload: Boolean
)

data class PlaylistItem(
    val duration: Int,
    val dataSize: Int,
    val modified: Long,
    val creativeLabel: String,
    val playlistKey: String,
    val creativeKey: String,
    val orderKey: Int
)