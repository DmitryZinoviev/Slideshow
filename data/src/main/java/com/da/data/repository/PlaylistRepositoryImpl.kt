package com.da.data.repository

import com.da.data.local.dao.PlaylistDao
import com.da.domain.repository.PlaylistRepository

class PlaylistRepositoryImpl(val playlistDao: PlaylistDao): PlaylistRepository {
}