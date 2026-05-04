package com.example.health_measure_application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.health_measure_application.data.HealthMetrics
import com.example.health_measure_application.data.MockHealthRepository
import com.example.health_measure_application.data.RecentExercise
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SessionState {
    INITIAL,
    ACTIVE,
    PAUSED
}

data class HealthUiState(
    val sessionState: SessionState = SessionState.INITIAL,
    val healthMetrics: HealthMetrics = HealthMetrics(0, 0, 0, 0.0, 0),
    val healthServiceAvailable: Boolean = true,
    val recentExercises: List<RecentExercise> = emptyList()
)

class HealthViewModel(
    private val repository: MockHealthRepository = MockHealthRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        HealthUiState(recentExercises = repository.recentExercises())
    )
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    private var sessionJob: Job? = null

    fun onStartEnd() {
        when (_uiState.value.sessionState) {
            SessionState.INITIAL -> startSession()
            SessionState.ACTIVE,
            SessionState.PAUSED -> endSession()
        }
    }

    fun onPauseResume() {
        when (_uiState.value.sessionState) {
            SessionState.ACTIVE -> pauseSession()
            SessionState.PAUSED -> resumeSession()
            SessionState.INITIAL -> Unit
        }
    }

    fun onRetryHealthService() {
        _uiState.update { it.copy(healthServiceAvailable = true) }
    }

    private fun startSession() {
        val initialMetrics = HealthMetrics(0, 80, 0, 0.0, 0)
        _uiState.update {
            it.copy(sessionState = SessionState.ACTIVE, healthMetrics = initialMetrics)
        }
        startCollecting(initialMetrics)
    }

    private fun resumeSession() {
        val current = _uiState.value.healthMetrics
        _uiState.update { it.copy(sessionState = SessionState.ACTIVE) }
        startCollecting(current)
    }

    private fun pauseSession() {
        sessionJob?.cancel()
        sessionJob = null
        _uiState.update { it.copy(sessionState = SessionState.PAUSED) }
    }

    private fun endSession() {
        sessionJob?.cancel()
        sessionJob = null
        _uiState.update {
            it.copy(
                sessionState = SessionState.INITIAL,
                healthMetrics = HealthMetrics(0, 0, 0, 0.0, 0)
            )
        }
    }

    private fun startCollecting(startMetrics: HealthMetrics) {
        sessionJob?.cancel()
        sessionJob = viewModelScope.launch {
            repository.metricsFlow(startMetrics).collect { metrics ->
                _uiState.update { state ->
                    state.copy(
                        sessionState = SessionState.ACTIVE,
                        healthMetrics = metrics
                    )
                }
            }
        }
    }

    override fun onCleared() {
        sessionJob?.cancel()
        super.onCleared()
    }
}

