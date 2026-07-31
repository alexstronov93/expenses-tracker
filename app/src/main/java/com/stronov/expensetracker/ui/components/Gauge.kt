package com.stronov.expensetracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stronov.expensetracker.ui.theme.DuetlyMotion

/**
 * The Safe-to-spend gauge: a 180° arc where [fraction] is filled from the RIGHT
 * end, the rest is track, and a short thumb marks the boundary. Center [content]
 * carries the label and amount.
 */
@Composable
fun SemiGauge(
    fraction: Float,
    trackColor: Color,
    fillColor: Color,
    thumbColor: Color,
    modifier: Modifier = Modifier,
    width: Dp = 236.dp,
    stroke: Dp = 15.dp,
    content: @Composable () -> Unit,
) {
    val animated by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = tween(DuetlyMotion.SLOW),
        label = "gauge",
    )
    val height = width / 2 + stroke

    Box(modifier = modifier.width(width).height(height), contentAlignment = Alignment.TopCenter) {
        Canvas(modifier = Modifier.width(width).height(height)) {
            val strokePx = stroke.toPx()
            val diameter = size.width - strokePx
            val topLeft = Offset(strokePx / 2f, strokePx / 2f)
            val arcSize = Size(diameter, diameter)
            val cap = Stroke(width = strokePx, cap = StrokeCap.Round)

            // Track across the top, left -> right.
            drawArc(
                color = trackColor, startAngle = 180f, sweepAngle = 180f, useCenter = false,
                topLeft = topLeft, size = arcSize, style = cap,
            )
            // Filled portion anchored at the right end.
            val fillSweep = 180f * animated
            val fillStart = 360f - fillSweep
            if (fillSweep > 0.5f) {
                drawArc(
                    color = fillColor, startAngle = fillStart, sweepAngle = fillSweep, useCenter = false,
                    topLeft = topLeft, size = arcSize, style = cap,
                )
            }
            // Thumb at the boundary.
            drawArc(
                color = thumbColor, startAngle = fillStart - 5f, sweepAngle = 8f, useCenter = false,
                topLeft = topLeft, size = arcSize, style = cap,
            )
        }

        Box(
            modifier = Modifier.width(width).height(height).padding(top = width / 5),
            contentAlignment = Alignment.TopCenter,
        ) { content() }
    }
}
