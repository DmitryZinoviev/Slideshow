package com.da.domain.model

sealed class ScreenResult {
    data class Success(val screen: Screen) : ScreenResult()
    data object NotFound : ScreenResult()
    data class Error(val exception: Exception) : ScreenResult()
}