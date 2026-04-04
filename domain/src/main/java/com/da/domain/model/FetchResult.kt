package com.da.domain.model

sealed class FetchResult {
    data object Fetch: FetchResult()
    data object Skip: FetchResult()
    data object Error: FetchResult()
}