package com.example.health_measure_application.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class MockHealthRepository {
    fun metricsFlow(initial: HealthMetrics): Flow<HealthMetrics> = flow {
        var metrics = initial
        val random = Random(System.currentTimeMillis())
        while (true) {
            delay(1000L)
            metrics = metrics.advance(random)
            emit(metrics)
        }
    }

    fun recentExercises(): List<RecentExercise> {
        return listOf(
            RecentExercise("Morning Run", 24, 230, 2.4),
            RecentExercise("Intervals", 18, 190, 1.6),
            RecentExercise("Evening Walk", 32, 160, 2.1)
        )
    }
}

