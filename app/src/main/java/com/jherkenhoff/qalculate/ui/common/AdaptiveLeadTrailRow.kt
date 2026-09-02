package com.jherkenhoff.qalculate.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveLeadTrailRow(
    leading: @Composable () -> Unit,
    trailing: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 2.dp,
) {
    Layout(
        contents = listOf(leading, trailing),
        modifier = modifier
    ) { (leadingMeasurables, trailingMeasurables), constraints ->
        val leadingMeasurable = leadingMeasurables.first()
        val trailingMeasurable = trailingMeasurables.first()

        val hSpacingPx = horizontalSpacing.roundToPx()
        val vSpacingPx = verticalSpacing.roundToPx()

        // Measure unconstrained (intrinsic width) first, to see if they'd fit in a row
        val leadingPlaceable = leadingMeasurable.measure(constraints.copyMaxDimensions())
        val trailingPlaceable = trailingMeasurable.measure(constraints.copyMaxDimensions())

        val combinedWidth = leadingPlaceable.width + trailingPlaceable.width + hSpacingPx
        val fitsInRow = combinedWidth <= constraints.maxWidth

        if (fitsInRow) {
            // Row layout — reuse the intrinsic measurements, just place them
            val height = maxOf(leadingPlaceable.height, trailingPlaceable.height)
            layout(constraints.maxWidth, height) {
                leadingPlaceable.place(0, (height - leadingPlaceable.height) / 2)
                trailingPlaceable.place(
                    constraints.maxWidth - trailingPlaceable.width,
                    (height - trailingPlaceable.height) / 2
                )
            }
        } else {
            val height = leadingPlaceable.height + vSpacingPx + trailingPlaceable.height

            layout(constraints.maxWidth, height) {
                leadingPlaceable.place(0, 0)
                trailingPlaceable.place(constraints.maxWidth-trailingPlaceable.width, leadingPlaceable.height + vSpacingPx)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, name = "Inline, varying heights 1")
@Composable
private fun InlineVaryingHeights1Preview() {
    AdaptiveLeadTrailRow(
        leading = { Box(Modifier.background(Color.Red).size(200.dp, 40.dp)) },
        trailing = { Box(Modifier.background(Color.Green).size(100.dp, 20.dp)) }
    )
}

@Preview(showBackground = true, widthDp = 400, name = "Inline, varying heights 2")
@Composable
private fun InlineVaryingHeights2Preview() {
    AdaptiveLeadTrailRow(
        leading = { Box(Modifier.background(Color.Red).size(200.dp, 20.dp)) },
        trailing = { Box(Modifier.background(Color.Green).size(100.dp, 40.dp)) }
    )
}

@Preview(showBackground = true, widthDp = 400, name = "Overflow, varying heights 1")
@Composable
private fun OverflowVaryingHeights1Preview() {
    AdaptiveLeadTrailRow(
        leading = { Box(Modifier.background(Color.Red).size(300.dp, 20.dp)) },
        trailing = { Box(Modifier.background(Color.Green).size(200.dp, 40.dp)) }
    )
}