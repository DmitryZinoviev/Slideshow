package com.da.slideshow.ui.main

import com.da.domain.model.PlaylistItemForReplay

data class MainState(
    val screenKey: String,
    val isLoading: Boolean,
    val blockScreenKeyUpdate: Boolean,
    val path: String,
    val list: List<PlaylistItemForReplay>
)