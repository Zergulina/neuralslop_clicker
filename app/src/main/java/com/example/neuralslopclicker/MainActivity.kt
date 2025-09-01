package com.example.neuralslopclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neuralslopclicker.ui.theme.NeuralslopClickerTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.draw.rotate
import kotlinx.coroutines.isActive
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeuralslopClickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameContainer(
                        modifier = Modifier.padding(innerPadding).fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun GameContainer(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var angle by remember { mutableStateOf(0f) }
        var velocity by remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            while (isActive) {
                withFrameNanos { frameTime ->
                    angle = (angle + velocity) % 360f

                    if (abs(velocity) > 0.01f) {
                        velocity *= 0.98f
                    } else {
                        velocity = 0f
                    }
                }
            }
        }

        Button(
            onClick = {velocity+= if (velocity < 3f) 1f else 2f},
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape,
            modifier = Modifier.size(64.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.videocard_nv1),
                contentDescription = "Video card NV1",
                modifier= Modifier
                    .size(64.dp * 0.8f)
                    .rotate(angle)
            )
        }
    }
}

