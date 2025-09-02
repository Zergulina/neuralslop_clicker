package com.example.neuralslopclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs
import kotlin.math.pow

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
    var score by remember { mutableStateOf(0.toDouble()) }
    val buffList: List<Buff> = listOf(
        Buff(name="Good boy clever boy", imageResourseId = R.drawable.neuralslop_octupus_kid, _level = 0, _price = 100),
        Buff(name="Shark plane escape", imageResourseId = R.drawable.neuralslop_sharkplane, _level = 0, _price = 1000),
        Buff(name="Happy birthday", imageResourseId = R.drawable.neuralslop_grandma, _level = 0, _price = 10000),
        Buff(name="Beautiful", imageResourseId = R.drawable.neuralslop_farmergirl, _level = 0, _price = 50000),
        Buff(name="Meow meow meow", imageResourseId = R.drawable.neuralslop_meowmeow, _level = 0, _price = 100000),
        Buff(name="Tralalero tralala", imageResourseId = R.drawable.neuralslop_tralalerotralala, _level = 0, _price = 250000),
        Buff(name="Dikiy skebob", imageResourseId = R.drawable.neuralslop_dikiyskebob, _level = 0, _price = 1000000)
    )

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                score += buffList.sumOf { it.profit }
                delay(1000)
            }
        }
    }

    Column (
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text (
            text = score.toInt().toString(),
            modifier = Modifier.padding(10.dp),
            fontSize = 40.sp
        )
        BoxWithConstraints(modifier = Modifier, contentAlignment = Alignment.Center) {
            val minSide = minOf(maxWidth, maxHeight)
            Spinner(modifier = Modifier.size(minSide * 0.8f), onClick = {score += 1})
        }
        Column (modifier = Modifier.height(150.dp).fillMaxWidth()) {
            LazyRow (modifier = Modifier.fillMaxHeight()) {
                items(buffList) { buff ->
                    BuffCard(Modifier.fillMaxHeight().width(120.dp), buff, score >= buff.price, clickOn = {
                        if (score >= buff.price) {
                            buff.level += 1
                            score -= buff.price
                            buff.price = (buff._price * 1.25.pow(buff.level)).toInt()
                            buff.profit = buff._profit * 1.1.pow(buff.level - 1)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun Spinner(modifier: Modifier, onClick: () -> Unit) {
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
        onClick = {
            velocity+= if (velocity < 3f) 1f else 2f
            onClick()
        },
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.videocard_nv1),
            contentDescription = "Video card NV1",
            modifier= Modifier
                .fillMaxSize(0.8f)
                .rotate(angle)
        )
    }
}

data class Buff(
    val name: String,
    @DrawableRes val imageResourseId: Int,
    val _level: Int,
    val _price: Int,
    ){
    var level by mutableStateOf(_level)
    var price by mutableStateOf(_price)
    val _profit = (_price / 100).toDouble()
    var profit by mutableStateOf(0.toDouble())
}

@Composable
fun BuffCard(modifier: Modifier, buff: Buff, canUpgrade: Boolean, clickOn: () -> Unit) {
    Column ( modifier = modifier.border(shape = RectangleShape, width = 1.dp, color = Color.Black) ) {
        Box (
            modifier= Modifier
                .weight(1f)
                .fillMaxSize()
                .clickable{clickOn()}
        ) {
            Image(
                painter = painterResource(buff.imageResourseId),
                contentDescription = "Octopus kid",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize()
            )
            val borderShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 10.dp, bottomEnd = 0.dp)
            Text(
                text = "${buff.level} LVL",
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .border(border = BorderStroke(1.dp, Color.Black), shape = borderShape)
                    .clip(shape = borderShape)
                    .background(if (canUpgrade) Color.Green else Color(225, 215, 0))
                    .padding(3.dp)
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = buff.name,
            fontSize = 12.sp
        )
        Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 0.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text (
                text = "${String.format("%.2f", buff.profit)} $/s",
                        fontSize = 12.sp
            )
            Text (
                text = "$${buff.price}",
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
fun BuffCardPreview() {
    BuffCard(modifier = Modifier.height(150.dp).width(120.dp), buff = Buff( name = "Aboba bebebe", _level = 4, _price = 10, imageResourseId = R.drawable.neuralslop_octupus_kid), true, {})
}