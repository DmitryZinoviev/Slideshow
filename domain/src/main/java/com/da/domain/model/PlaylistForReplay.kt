package com.da.domain.model

sealed class PlaylistForReplayResult(){
    data object Fail: PlaylistForReplayResult()
    data class Success(val playlistForReplay: PlaylistForReplay): PlaylistForReplayResult()
}

data class PlaylistForReplay (
    val screenKey: String,
    val items: List<PlaylistItemForReplay>

)

data class PlaylistItemForReplay (
    val order:Int,
    val path: String,
    val isVideo: Boolean,
    val durationSec: Int
    )
