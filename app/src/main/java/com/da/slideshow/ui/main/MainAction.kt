package com.da.slideshow.ui.main

sealed class MainAction {
    data class ChangeScreenKeyAction(val screenKey: String) : MainAction()
    data class FetchScreenAction(val screenKey: String) : MainAction()
    data class StartReplayAction(val screenKey: String) : MainAction()
    data object StopReplayAction : MainAction()
    data object SkipItemAction : MainAction()
}
