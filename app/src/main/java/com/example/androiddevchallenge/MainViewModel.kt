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

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {

    private var timerInMillis: Long = 0L
    private val TAG: String? = "MainViewModel"
    var timer: CountDownTimer? = null
    private val _counter = MutableLiveData(Counter())
    val counter: LiveData<Counter> = _counter

    fun startTimer(timerInMillis: Long) {
        if (timer != null)
            timer?.cancel()
        this.timerInMillis = timerInMillis * 1000L
        Log.v(TAG, "Timer started" + this.timerInMillis)
        // initialize timer and start
        timer = object : CountDownTimer(timerInMillis * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTimerTick(millisUntilFinished)
            }

            override fun onFinish() {
                /*handle finishing of a timer
            * start new one if there are still repetition left*/
                onTimerFinish()
            }
        }.start()
    }

    private fun onTimerFinish() {
        _counter.value = Counter(value = "Lift Off!")
    }

    private fun onTimerTick(millisUntilFinished: Long) {
        var remTime: String = ""
        remTime = if (TimeUnit.MILLISECONDS.toHours(millisUntilFinished)> 0) {
            String.format(
                "%d:%d:%d",
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                ),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                )
            )
        } else {
            String.format(
                "%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
            )
        }
        _counter.value = Counter(value = remTime, progress = 100 - (millisUntilFinished * 100 / timerInMillis).toInt())
    }
}
