package com.example.speedtest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.speedtest.ui.theme.ComposeSpeedTestTheme
import com.example.speedtest.ui.theme.DarkColor
import com.example.speedtest.ui.theme.DarkGradient
import com.example.speedtest.ui.theme.Green200
import com.example.speedtest.ui.theme.Green500
import com.example.speedtest.ui.theme.GreenGradient
import com.example.speedtest.ui.theme.LightColor
import kotlin.math.floor

@Composable
fun SpeedTestScreen() {

    Column(Modifier.fillMaxSize()) {

    }

}

@Composable
private fun SpeedTestScreen(state: UiState, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header()
        SpeedIndicator(state = state, onClick = onClick)
        AdditionalInfo(state.ping, state.maxSpeed)
        NavigationView()
    }
}

@Composable
fun SpeedIndicator(state: UiState, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        CircularSpeedIndicator(state.arcValue, 240f)

    }
}

@Composable
fun CircularSpeedIndicator(value: Float, angle: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        drawLines(value, angle)
        drawArcs(value, angle)
    }
}

//drawing indication lines
fun DrawScope.drawLines(progress: Float, maxValue: Float, numberOfLines: Int = 40) {
    val oneRotation = maxValue / numberOfLines
    val startValue = if (progress == 0f) 0 else floor(progress * numberOfLines).toInt() + 1

    for (i in startValue..numberOfLines) {
        rotate(i * oneRotation + (180 - maxValue) / 2) {
            drawLine(
                LightColor,
                Offset(if (i % 5 == 0) 80f else 30f, size.height / 2),
                Offset(0f, size.height / 2),
                8f,
                StrokeCap.Round
            )
        }

    }
}

//arcs with nested functions
fun DrawScope.drawArcs(progress: Float, maxValue: Float) {
    val startAngle = 270 - maxValue / 2
    val sweepAngle = maxValue * progress

    val topLeft = Offset(50f, 50f)
    val size = Size(size.width - 100f, size.height - 100f)

    fun drawBlur() {
        for (i in 0..20) {
            drawArc(
                color = Green200.copy(alpha = i / 900f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)
            )
        }
    }

    fun drawStroke() {
        drawArc(
            color = Green500,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 86f, cap = StrokeCap.Round)
        )
    }
    //gradient color giving new view o arc
    fun drawGradient() {
        drawArc(
            brush = GreenGradient,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 80f, cap = StrokeCap.Round)
        )
    }

    //init functions
    drawBlur()
    drawStroke()
    drawGradient()
}


@Composable
fun NavigationView() {
    val items = listOf(
        R.drawable.wifi,
        R.drawable.person,
        R.drawable.speed,
        R.drawable.settings,
    )
    val selectedItem = 2

    BottomNavigation(backgroundColor = DarkColor) {
        items.mapIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedItem,
                onClick = { },
                unselectedContentColor = MaterialTheme.colors.onSurface,
                icon = {
                    Icon(painterResource(id = item), null)
                }
            )
        }
    }
}

@Composable
fun AdditionalInfo(ping: String, maxSpeed: String) {

    @Composable
    fun RowScope.InfoColumn(title: String, value: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title)
            Text(
                text = value,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(vertical = 8.dp)
            )

        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        InfoColumn(title = "PING", value = ping)
        VerticalDivider()
        InfoColumn(title = "MAX SPEED", value = maxSpeed)
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF414D66))
            .width(1.dp)
    )
}

@Composable
fun Header() {
    Text(
        text = "SPEEDTEST",
        modifier = Modifier.padding(top = 52.dp, bottom = 16.dp),
        style = MaterialTheme.typography.h6
    )
}

@Preview(showBackground = true)
@Composable
fun SpeedTestScreenPreview() {
    ComposeSpeedTestTheme {
        Surface {
            SpeedTestScreen(
                UiState(
                    speed = "120.5",
                    ping = "5 ms",
                    maxSpeed = "150.0 mbps",
                    arcValue = 0.25f
                )
            ) {

            }
        }
    }
}