package com.example.nosedivesim

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nosedivesim.ui.theme.NoseDiveSimTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoseDiveSimTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        StarRow(context = LocalContext.current)
                    }
                }
            }
        }
    }
}

@Composable
fun StarRow(context: Context, modifier: Modifier = Modifier) {
    val mediaPlayerLow = MediaPlayer.create(context, R.raw.second2)
    val mediaPlayerHigh = MediaPlayer.create(context, R.raw.first2)
    var playLow = remember {
        mutableStateOf(false)
    }

    var starCount = rememberSaveable {
        mutableStateOf(0)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val screenWidth = size.width
                    val dragPercentage = (dragAmount.x / screenWidth) * 100
                    if (dragPercentage > 0) {
                        starCount.value = 5
                        playLow.value = false
                    } else {
                        starCount.value = 1
                        playLow.value = true
                    }
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var notFirstTime = false
            for (i in 1..starCount.value) {
                Star(color = Color.Yellow, modifier = Modifier.size(60.dp))
                notFirstTime = true
            }
            for (i in 1..(5 - starCount.value)) {
                Star(color = Color.Gray, modifier = Modifier.size(60.dp))
            }
            if (notFirstTime) {
                if (playLow.value) {
                    LaunchedEffect(mediaPlayerLow) {
                        delay(900)
                        mediaPlayerLow.start()
                    }
                } else {
                    LaunchedEffect(mediaPlayerHigh) {
                        delay(900)
                        mediaPlayerHigh.start()
                    }
                }
            }

        }
    }
}

@Composable
fun Star(modifier: Modifier = Modifier, color: Color) {
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Star Icon",
        tint = color,
        modifier = modifier
    )
}

@Preview
@Composable
fun RatingPreview() {
    NoseDiveSimTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                StarRow(LocalContext.current)
            }
        }
    }
}