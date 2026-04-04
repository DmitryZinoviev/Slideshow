package com.da.slideshow.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da.data.local.db.dao.ScreenDao
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
    private val screenDao: ScreenDao
): ViewModel() {

    init {
        viewModelScope.launch {
            val key = getScreenKeyUseCase.invoke()
            _state.update {
                it.copy(screenKey = key)
            }
        }
    }

    private val _state = MutableStateFlow(
        MainState(
            screenKey = "",
            isLoading = false
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
                    val s = screenDao.getScreens()
                    val l = screenDao.getPlaylists()
                    val i = screenDao.getItems()
                    println("s = ${s.size}; p = ${l.size};i = ${i.size}")
                }

            }
            is MainAction.SyncAction -> {
                viewModelScope.launch(Dispatchers.IO) {
                    syncScreenUseCase.invoke(action.screenKey)
                }
            }
        }
    }
}