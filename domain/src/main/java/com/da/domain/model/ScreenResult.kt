package com.da.domain.model

import com.da.domain.core.ApiResult

sealed class ScreenResult {
    data class Found(val screen: Screen) : ScreenResult()
    data object NotFound : ScreenResult()
    data class Fail(val exception: Exception) : ScreenResult()
}