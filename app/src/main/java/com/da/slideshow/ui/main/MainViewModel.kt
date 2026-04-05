package com.da.slideshow.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da.data.downloader.DownloadWorkerObserverImpl
import com.da.data.local.db.dao.ScreenDao
import com.da.domain.useCases.CheckPendingDownloadUseCase
import com.da.domain.useCases.CleanTempFilesUseCase
import com.da.domain.useCases.GetScreenKeyUseCase
import com.da.domain.useCases.SaveScreenKeyUseCase
import com.da.domain.useCases.SyncScreenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val getScreenKeyUseCase: GetScreenKeyUseCase,
    private val saveScreenKeyUseCase: SaveScreenKeyUseCase,
    private val syncScreenUseCase: SyncScreenUseCase,
    private val checkPendingDownloadUseCase: CheckPendingDownloadUseCase,
    private val cleanTempFilesUseCase: CleanTempFilesUseCase
): ViewModel() {

    init {
        viewModelScope.launch {
            val key = getScreenKeyUseCase()
            _state.update {
                it.copy(screenKey = key)
            }
        }

        viewModelScope.launch {
            cleanTempFilesUseCase()
            checkPendingDownloadUseCase()
        }
    }

    private val _state = MutableStateFlow(
        MainState(
            screenKey = "",
            isLoading = false,
            path = ""
        )
    )

    val state: StateFlow<MainState> = _state

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.ChangeScreenKeyAction -> {

                viewModelScope.launch(Dispatchers.IO) {
                    saveScreenKeyUseCase.invoke(action.screenKey)

                    withContext(Dispatchers.Main) {
                        _state.update {
                            it.copy(screenKey = action.screenKey)
                        }
                    }
                }

            }

            MainAction.LoadAction -> {
                viewModelScope.launch(Dispatchers.IO) {
//                    val s = screenDao.getScreens()
//                    val l = screenDao.getPlaylists()
//                    val i = screenDao.getItems()
                    //val downloads = screenDao.getDownloads()
//                    println("s = ${s.size}; p = ${l.size};i = ${i.size}")
                    //downloadWorkerObserver.start()

//                    for (d in downloads){
//                        d.localPath?.let{ p ->
//                            withContext(Dispatchers.Main) {
//                                _state.update { it.copy(path = p) }
//                            }
//                        }
//                        delay(5000)
//
//                    }
                }

            }
            is MainAction.SyncAction -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    withContext(Dispatchers.IO) {
                        syncScreenUseCase.invoke(action.screenKey)
                    }

                    _state.update {
                        it.copy(isLoading = false)
                    }

                }
            }
        }
    }
}