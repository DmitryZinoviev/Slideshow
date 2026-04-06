package com.da.slideshow.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da.domain.model.FetchResult
import com.da.domain.model.PlaylistForReplay
import com.da.domain.model.PlaylistForReplayResult
import com.da.domain.model.PlaylistItemForReplay
import com.da.domain.useCases.CheckPendingDownloadUseCase
import com.da.domain.useCases.CleanTempFilesUseCase
import com.da.domain.useCases.DownloadResult
import com.da.domain.useCases.DownloadScreenPlaylistsUseCase
import com.da.domain.useCases.GetPlaylistForReplayUseCase
import com.da.domain.useCases.GetScreenKeyUseCase
import com.da.domain.useCases.SaveScreenKeyUseCase
import com.da.domain.useCases.SyncScreenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class MainViewModel(
    private val getScreenKeyUseCase: GetScreenKeyUseCase,
    private val saveScreenKeyUseCase: SaveScreenKeyUseCase,
    private val syncScreenUseCase: SyncScreenUseCase,
    private val checkPendingDownloadUseCase: CheckPendingDownloadUseCase,
    private val cleanTempFilesUseCase: CleanTempFilesUseCase,
    private val getPlaylistForReplayUseCase: GetPlaylistForReplayUseCase,
    private val downloadScreenPlaylistsUseCase: DownloadScreenPlaylistsUseCase

) : ViewModel() {

    private var isPlaying = false

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

        //onAction(MainAction.StartReplayAction)
    }


    private suspend fun startReplay(screenKey: String) {

        val result = getPlaylistForReplayUseCase(screenKey)

        when (result) {
            PlaylistForReplayResult.Fail -> {
                //show error
            }

            is PlaylistForReplayResult.Success -> {
                _state.update { it.copy(list = result.playlistForReplay.items) }
                //loop(result.playlistForReplay)
            }
        }
    }

//    private suspend fun loop(playlistForReplay: PlaylistForReplay) {
//        if (!isPlaying) {
//            isPlaying = true
//
//            while (isPlaying) {
//                for (i in playlistForReplay.items){
//                    _state.update { it.copy(path = i.path) }
//                    delay(i.fadeDuration.seconds)
//                }
//            }
//        }
//
//    }


    private fun stopReplay() {
        if (isPlaying) {
            isPlaying = false
        }
    }


    private val _state = MutableStateFlow(
        MainState(
            screenKey = "",
            isLoading = false,
            blockScreenKeyUpdate = false,
            path = "",
            list = emptyList()
        )
    )

    val state: StateFlow<MainState> = _state

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.ChangeScreenKeyAction -> {

                viewModelScope.launch() {
                    changeScreenKey(action)
                }
            }


            is MainAction.FetchScreenAction -> {
                viewModelScope.launch {
                    fetchScreen(action)
                }
            }

            MainAction.SkipItemAction -> TODO()
            is MainAction.StartReplayAction -> {

                viewModelScope.launch {
                    startReplay(action.screenKey)
                }
            }

            MainAction.StopReplayAction -> TODO()
        }
    }

    private suspend fun changeScreenKey(
        action: MainAction.ChangeScreenKeyAction
    ) = withContext(Dispatchers.IO) {
        saveScreenKeyUseCase(action.screenKey)

        withContext(Dispatchers.Main) {
            _state.update {
                it.copy(screenKey = action.screenKey)
            }
        }
    }

    private suspend fun fetchScreen(action: MainAction.FetchScreenAction) {
        _state.update {
            it.copy(blockScreenKeyUpdate = true)
        }
        withContext(Dispatchers.IO) {
            val res = syncScreenUseCase(action.screenKey)
            if (res is FetchResult.Fetch || res is FetchResult.Skip) {
                val downloadResult = downloadScreenPlaylistsUseCase(action.screenKey)
                if (downloadResult is DownloadResult.Complete || downloadResult is DownloadResult.Partial) {
                    onAction(MainAction.StartReplayAction(screenKey = action.screenKey))
                }
            }
        }

        _state.update {
            it.copy(blockScreenKeyUpdate = false)
        }
    }
}