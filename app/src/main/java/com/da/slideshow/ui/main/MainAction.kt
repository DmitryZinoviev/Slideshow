package com.da.slideshow.ui.main

sealed class MainAction {
    data class ChangeScreenKeyAction(val screenKey: String) : MainAction()
    data object LoadAction : MainAction()
}
