package com.da.domain.useCases

import com.da.domain.repository.UserPreferencesRepository

class SaveScreenKeyUseCase(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(key: String) {
        repository.saveScreenKey(key)
    }
}