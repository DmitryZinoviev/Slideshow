package com.da.domain.useCases

import com.da.domain.model.Download
import com.da.domain.model.ScreenResult
import javax.print.attribute.standard.QueuedJobCount

sealed class DownloadResult {
    data object Complete : DownloadResult()
    data class Partial(val successCount: Int, val totalCount: Int) : DownloadResult()
    data object Error : DownloadResult()
}
