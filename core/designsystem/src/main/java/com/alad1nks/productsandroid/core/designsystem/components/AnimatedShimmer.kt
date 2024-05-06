package com.alad1nks.productsandroid.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alad1nks.productsandroid.core.designsystem.R

object AnimatedShimmerDefaults {
    private val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    private val transition: InfiniteTransition @Composable get() =
        rememberInfiniteTransition(label = stringResource(R.string.infinite_transition))

    private val translateAnim: State<Float> @Composable get() =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = stringResource(R.string.float_animation)
        )

    val brush: Brush @Composable get() =
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnim.value, y = translateAnim.value)
        )
}

@Composable
fun AnimatedShimmerListItem(
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Spacer(
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(30))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(AnimatedShimmerDefaults.brush)
            )
        },
        modifier = modifier,
        supportingContent = {
            Spacer(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(30))
                    .fillMaxWidth(fraction = 0.9f)
                    .background(AnimatedShimmerDefaults.brush)
            )
        },
        leadingContent = {
            Spacer(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30))
                    .background(AnimatedShimmerDefaults.brush)
            )
        }
    )
}
