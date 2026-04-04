package com.da.data.remote.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistItemsResponse(
    val duration: Int?,
    val dataSize: Int?,
    val expireDate: String?,
    val startDate: String?,
    val modified: Long?,
    val selectedReccurenceDays: Int?,
    val collectStatistics: Boolean?,
    val creativeProperties: String?,
    val creativeLabel: String?,
    val slidePriority: Int?,
    val playlistKey: String?,
    val creativeKey: String?,
    val orderKey: Int?,
    val eventTypesList: List<Any>
)