package com.example.health_measure_application.data

import kotlin.random.Random

private const val MinHeartRate = 70
private const val MaxHeartRate = 180
private const val LapDistanceKm = 0.25

data class HealthMetrics(
    val elapsedSeconds: Int,
    val heartRate: Int,
    val calories: Int,
    val distanceKm: Double,
    val laps: Int
) {
    fun advance(random: Random): HealthMetrics {
        val nextElapsed = elapsedSeconds + 1
        val delta = random.nextInt(-4, 5)
        val nextHeartRate = (heartRate + delta).coerceIn(MinHeartRate, MaxHeartRate)
        val intensity = (nextHeartRate - 60).coerceAtLeast(10)
        val nextCalories = calories + (intensity / 20).coerceAtLeast(1)
        val nextDistance = if (nextElapsed % 3 == 0) distanceKm + 0.01 else distanceKm
        val nextLaps = if (nextDistance >= (laps + 1) * LapDistanceKm) laps + 1 else laps

        return copy(
            elapsedSeconds = nextElapsed,
            heartRate = nextHeartRate,
            calories = nextCalories,
            distanceKm = nextDistance,
            laps = nextLaps
        )
    }
}

data class RecentExercise(
    val title: String,
    val durationMinutes: Int,
    val calories: Int,
    val distanceKm: Double
)

