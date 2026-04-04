package com.da.domain.useCases

import com.da.domain.repository.UserPreferencesRepository

class GetScreenKeyUseCase(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(): String {
        return repository.getScreenKey()
    }
}