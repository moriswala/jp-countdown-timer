/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val TIMER_IN_SECONDS = 30L // Timer in seconds animation handled dynamically based on time specified.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme(darkTheme = false) {
                Scaffold(
                    Modifier.background(color = MaterialTheme.colors.primarySurface,),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.app_name,),
                                    style = MaterialTheme.typography.h5, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                                )
                            },

                            backgroundColor = MaterialTheme.colors.primarySurface,
                            contentColor = Color.White,
                            elevation = 12.dp
                        )
                    },
                    content = { MyTimeApp() }
                )
            }
        }
        viewModel.startTimer(TIMER_IN_SECONDS)
    }

    @Composable
    fun MyTimeApp() {
        TimerScreen(
            viewModel = viewModel,
            onResetClick = {
                recreate() // todo, I was forced to use recreate(), as I could not restart lottie animation in jetpack compose

                // restart lottie animation here
                // viewModel.startTimer(TIMER_IN_SECONDS)
            }
        )
    }
    @Composable
    private fun TimerScreen(viewModel: MainViewModel, onResetClick: () -> Unit) {
        TimerView(viewModel = viewModel, onResetClick = onResetClick)
    }

    @Composable
    fun TimerView(viewModel: MainViewModel, modifier: Modifier = Modifier, onResetClick: () -> Unit) {
        val counter: Counter? by viewModel.counter.observeAsState()
        val animationSpec = remember { LottieAnimationSpec.RawRes(R.raw.lottie_hourglass) }
        val animationState = rememberLottieAnimationState(autoPlay = true, repeatCount = 0)
        animationState.speed = 60 * .13f / TIMER_IN_SECONDS

        Column {
            // Adds view to Compose
            LottieAnimation(
                animationSpec,
                modifier = modifier,
                animationState,
            )
            Text(
                "${counter?.value}", fontSize = 25.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        onResetClick.invoke()
                    }
                ) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.wrapContentHeight())
                    Text(
                        text = " Restart", fontSize = 25.sp,
                        modifier = Modifier.wrapContentSize(align = Alignment.Center),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
