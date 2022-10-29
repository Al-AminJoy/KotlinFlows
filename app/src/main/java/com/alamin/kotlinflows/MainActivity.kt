package com.alamin.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* CoroutineScope(Dispatchers.Main).launch {
             val data: Flow<Int> = producer()
             data.onStart {
                 emit(-1) //Additional Emit if Want
                     Log.d(TAG, "onCreate: onStart")
                 }
                 .onCompletion {
                     emit(6) //Additional Emit if Want
                     Log.d(TAG, "onCreate: onComplete")
                 }
                 .onEach {
                     Log.d(TAG, "onCreate: Before Emit $it")
                 }.collect{
                 Log.d(TAG, "onCreate: $it")
             }
         }*/
        CoroutineScope(Dispatchers.Main).launch {
            val data: Flow<Int> = producer()
            data.flowOn(Dispatchers.IO)
                .buffer(3) //Buffer used for hold item if consumer slow
                .map {
                    it * 2
                }
                .filter {
                    it < 10
                }
                .collect {
                    delay(1500)
                    Log.d(TAG, "onCreate: $it")
                }
        }
    }

    fun producer() = flow<Int> {
        val list = arrayListOf(1, 2, 3, 4, 5)
        list.forEach {
            delay(1000)
            emit(it)
            //throw Exception("Emitter Exception")
        }
    }.catch {
        Log.d(TAG, "producer: ${it.message}")
    }
}