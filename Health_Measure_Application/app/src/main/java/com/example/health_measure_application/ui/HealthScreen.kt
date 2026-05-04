package com.example.health_measure_application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.health_measure_application.data.HealthMetrics
import com.example.health_measure_application.data.RecentExercise
import com.example.health_measure_application.ui.theme.WearAccentOrange
import com.example.health_measure_application.ui.theme.WearDarkBackground
import com.example.health_measure_application.ui.theme.WearDarkSurface
import com.example.health_measure_application.ui.theme.WearMuted
import com.example.health_measure_application.ui.theme.WearOnDark
import com.example.health_measure_application.viewmodel.HealthUiState
import com.example.health_measure_application.viewmodel.HealthViewModel
import com.example.health_measure_application.viewmodel.SessionState
import java.util.Locale

@Composable
fun HealthApp(viewModel: HealthViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    HealthScreen(
        uiState = uiState,
        onStartEnd = viewModel::onStartEnd,
        onPauseResume = viewModel::onPauseResume,
        onRetryHealthService = viewModel::onRetryHealthService
    )
}

@Composable
fun HealthScreen(
    uiState: HealthUiState,
    onStartEnd: () -> Unit,
    onPauseResume: () -> Unit,
    onRetryHealthService: () -> Unit
) {
    Surface(color = WearDarkBackground, modifier = Modifier.fillMaxSize()) {
        if (!uiState.healthServiceAvailable) {
            HealthServiceUnavailable(onRetryHealthService)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ElapsedTimeHeader(uiState.healthMetrics)
                Spacer(modifier = Modifier.height(8.dp))
                MetricsGrid(uiState)
                Spacer(modifier = Modifier.height(10.dp))
                SessionControls(uiState.sessionState, onStartEnd, onPauseResume)
                Spacer(modifier = Modifier.height(10.dp))
                RecentExercises(uiState.recentExercises)
            }
        }
    }
}

@Composable
private fun ElapsedTimeHeader(metrics: HealthMetrics) {
    val minutes = metrics.elapsedSeconds / 60
    val seconds = metrics.elapsedSeconds % 60
    val formatted = String.format(Locale.US, "%02dm%02ds", minutes, seconds)

    Text(
        text = formatted,
        color = WearOnDark,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun MetricsGrid(uiState: HealthUiState) {
    val metrics = uiState.healthMetrics
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            MetricTile(
                title = "Heart Rate",
                value = formatMetricValue(uiState.sessionState, metrics.heartRate),
                unit = "bpm",
                icon = Icons.Filled.Favorite
            )
            MetricTile(
                title = "Calories",
                value = formatMetricValue(uiState.sessionState, metrics.calories),
                unit = "cal",
                icon = Icons.Filled.Whatshot
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            MetricTile(
                title = "Distance",
                value = if (uiState.sessionState == SessionState.INITIAL) "--" else {
                    String.format(Locale.US, "%.2f", metrics.distanceKm)
                },
                unit = "km",
                icon = Icons.AutoMirrored.Filled.TrendingUp
            )
            MetricTile(
                title = "Laps",
                value = formatMetricValue(uiState.sessionState, metrics.laps),
                unit = "rounds",
                icon = Icons.Filled.Loop
            )
        }
    }
}

@Composable
private fun RowScope.MetricTile(
    title: String,
    value: String,
    unit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .padding(4.dp)
            .border(1.dp, WearAccentOrange, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(WearDarkSurface)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = WearAccentOrange,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                color = WearOnDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = unit,
                color = WearAccentOrange,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun SessionControls(
    sessionState: SessionState,
    onStartEnd: () -> Unit,
    onPauseResume: () -> Unit
) {
    val startEndLabel = if (sessionState == SessionState.INITIAL) "START" else "END"
    val startEndColor = if (sessionState == SessionState.INITIAL) {
        WearAccentOrange
    } else {
        Color(0xFFD32F2F)
    }
    val pauseResumeLabel = if (sessionState == SessionState.PAUSED) "RESUME" else "PAUSE"
    val pauseResumeColor = when (sessionState) {
        SessionState.INITIAL -> Color(0xFF4D4D4D)
        SessionState.ACTIVE -> Color(0xFFFFC107)
        SessionState.PAUSED -> Color(0xFF4CAF50)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onStartEnd,
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(containerColor = startEndColor)
        ) {
            Text(text = startEndLabel, color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onPauseResume,
            enabled = sessionState != SessionState.INITIAL,
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            colors = ButtonDefaults.buttonColors(containerColor = pauseResumeColor)
        ) {
            Text(text = pauseResumeLabel, color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun RecentExercises(recentExercises: List<RecentExercise>) {
    if (recentExercises.isEmpty()) {
        return
    }

    Text(
        text = "Recent Exercises",
        color = WearAccentOrange,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 4.dp)
    )
    recentExercises.take(3).forEach { exercise ->
        RecentExerciseRow(exercise)
    }
}

@Composable
private fun RecentExerciseRow(exercise: RecentExercise) {
    val detail = String.format(
        Locale.US,
        "%dm  %.1fkm  %dcal",
        exercise.durationMinutes,
        exercise.distanceKm,
        exercise.calories
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = exercise.title, color = WearOnDark, fontSize = 11.sp)
        Text(text = detail, color = WearMuted, fontSize = 10.sp)
    }
}

@Composable
private fun HealthServiceUnavailable(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = "Health services unavailable",
            tint = WearAccentOrange,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Health Services not available",
            color = WearOnDark,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = WearAccentOrange)
        ) {
            Text(text = "RETRY", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

private fun formatMetricValue(sessionState: SessionState, value: Int): String {
    return if (sessionState == SessionState.INITIAL) "--" else value.toString()
}
