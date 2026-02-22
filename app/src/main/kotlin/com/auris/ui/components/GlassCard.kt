package com.auris.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.auris.ui.theme.AurisColors

/**
 * GlassCard — iOS 17 frosted glass surface.
 * rgba(255,255,255,0.72) fill, 20dp corner, 1px white border, md shadow.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    elevated: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val bg     = if (elevated) AurisColors.CardGlassElevated else AurisColors.CardGlass
    val border = if (elevated) AurisColors.CardBorderHigh    else AurisColors.CardBorder
    val elev   = if (elevated) 4.dp else 2.dp

    Box(
        modifier = modifier
            .shadow(elevation = elev, shape = RoundedCornerShape(20.dp), ambientColor = AurisColors.ShadowColor.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(20.dp)),
        content = content
    )
}
