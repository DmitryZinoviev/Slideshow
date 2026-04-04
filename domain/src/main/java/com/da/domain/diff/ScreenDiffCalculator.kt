package com.da.domain.diff

import com.da.domain.model.Playlist
import com.da.domain.model.PlaylistItem
import com.da.domain.model.Screen

class ScreenDiffCalculator {

    fun calculate(
        local: Screen,
        remote: Screen
    ): ScreenDiff {

        if(remote.modified == local.modified){
            return ScreenDiff(
                playlists = PlaylistDiffResult(emptyList(), emptyList(), emptyList()),
                isModifiedChanged = false
            )
        }

        val playlistsDiff = diffPlaylists(local.playlists, remote.playlists)
        return ScreenDiff(
            playlists = playlistsDiff,
            isModifiedChanged = true
        )
    }

    private fun diffPlaylists(
        local: List<Playlist>,
        remote: List<Playlist>
    ): PlaylistDiffResult {

        val localMap = local.associateBy { it.playlistKey }
        val remoteMap = remote.associateBy { it.playlistKey }

        val added = remote.filter { it.playlistKey !in localMap }
        val removed = local.filter { it.playlistKey !in remoteMap }

        val updated = remote.mapNotNull { remotePl ->
            val localPl = localMap[remotePl.playlistKey] ?: return@mapNotNull null

            val itemsDiff = diffItems(localPl.items, remotePl.items)

            if (itemsDiff.isEmpty()) return@mapNotNull null

            PlaylistItemsDiff(
                playlistKey = remotePl.playlistKey,
                added = itemsDiff.added,
                removed = itemsDiff.removed,
                updated = itemsDiff.updated
            )
        }

        return PlaylistDiffResult(added, removed, updated)
    }

    private fun diffItems(
        local: List<PlaylistItem>,
        remote: List<PlaylistItem>
    ): DiffResult<PlaylistItem> {

        val localMap = local.associateBy { it.creativeKey }
        val remoteMap = remote.associateBy { it.creativeKey }

        val added = remote.filter { it.creativeKey !in localMap }
        val removed = local.filter { it.creativeKey !in remoteMap }

        val updated = remote.filter { r ->
            val l = localMap[r.creativeKey]
            l != null && isItemUpdated(l, r)
        }

        return DiffResult(added, removed, updated)
    }

    private fun isItemUpdated(
        local: PlaylistItem,
        remote: PlaylistItem
    ): Boolean {
        return local.duration != remote.duration ||
                local.dataSize != remote.dataSize ||
                local.modified != remote.modified ||
                local.orderKey != remote.orderKey ||
                local.creativeLabel != remote.creativeLabel
    }
}