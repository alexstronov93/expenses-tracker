package com.stronov.expensetracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stronov.expensetracker.ui.theme.Duetly
import com.stronov.expensetracker.ui.theme.DuetlyMotion
import com.stronov.expensetracker.ui.theme.DuetlyType

/** Page gutter: --page-margin. */
val PageMargin = 24.dp

/** 2a chrome: flat 12px card, hairline border, no shadow. */
@Composable
fun DuetlyCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = Duetly.colors
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(c.surface)
            .border(1.dp, c.border, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        content = content,
    )
}

/** Uppercase tracked section label, optionally with a trailing action. */
@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    val c = Duetly.colors
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = PageMargin),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text.uppercase(), style = DuetlyType.sectionLabel, color = c.textSecondary, modifier = Modifier.weight(1f))
        if (action != null) {
            Text(
                action,
                style = DuetlyType.bodyStrong,
                color = c.textPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .then(if (onActionClick != null) Modifier.clickable(onClick = onActionClick) else Modifier)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            )
        }
    }
}

/**
 * The spent / held / safe bar. Segments are weighted by value, separated by a
 * small gap, and animate when money moves between them.
 */
@Composable
fun SegmentedBar(
    segments: List<Pair<Long, Color>>,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
) {
    val total = segments.sumOf { it.first }.coerceAtLeast(1L)
    Row(
        modifier = modifier.fillMaxWidth().height(height),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        segments.forEach { (value, color) ->
            val weight by animateFloatAsState(
                targetValue = (value.toFloat() / total).coerceAtLeast(0f),
                animationSpec = tween(DuetlyMotion.SLOW),
                label = "segment",
            )
            if (weight > 0.001f) {
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .height(height)
                        .clip(RoundedCornerShape(999.dp))
                        .background(color),
                )
            }
        }
    }
}

/** Legend dot + label + amount, used under the segmented bar. */
@Composable
fun LegendItem(color: Color, label: String, amount: String, modifier: Modifier = Modifier) {
    val c = Duetly.colors
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(8.dp))
        Text(label, style = DuetlyType.small, color = c.textSecondary)
        Spacer(Modifier.width(6.dp))
        Text(amount, style = DuetlyType.amountSm, color = c.textPrimary)
    }
}

/** Rounded square icon tile on the sunken surface. */
@Composable
fun IconTile(iconKey: String, size: Dp = 38.dp) {
    val c = Duetly.colors
    Box(
        modifier = Modifier.size(size).clip(RoundedCornerShape(10.dp)).background(c.sunken),
        contentAlignment = Alignment.Center,
    ) {
        Icon(iconFor(iconKey), null, tint = c.textPrimary, modifier = Modifier.size(19.dp))
    }
}

/** Hairline row divider, inset past the icon tile. */
@Composable
fun RowDivider(startInset: Dp = 68.dp) {
    val c = Duetly.colors
    Box(Modifier.fillMaxWidth().padding(start = startInset).height(1.dp).background(c.border))
}

fun iconFor(key: String): ImageVector = when (key) {
    "home" -> Icons.Outlined.Home
    "phone" -> Icons.Outlined.Smartphone
    "bolt" -> Icons.Outlined.Bolt
    "wifi" -> Icons.Outlined.Wifi
    "shield" -> Icons.Outlined.Shield
    "basket" -> Icons.Outlined.ShoppingBasket
    "restaurant" -> Icons.Outlined.Restaurant
    "transport" -> Icons.Outlined.DirectionsBus
    "fun" -> Icons.Outlined.ConfirmationNumber
    "household" -> Icons.Outlined.ShoppingBag
    else -> Icons.Outlined.ShoppingBag
}
