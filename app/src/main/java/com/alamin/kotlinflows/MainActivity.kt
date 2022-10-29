package com.alamin.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* CoroutineScope(Main).launch {
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
       /* CoroutineScope(Main).launch {
            val data: Flow<Int> = producer()
            data.flowOn(IO)
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
        }*/

/*        CoroutineScope(Main).launch {
            val data: Flow<Int> = sharedProducer()
            data.collect {
                    Log.d(TAG, "onCreate: 1 $it")
                }
        }
        CoroutineScope(Main).launch {
            val data: Flow<Int> = sharedProducer()
            delay(2500)
            data.collect {
                    Log.d(TAG, "onCreate: 2 $it")
                }
        }*/
        CoroutineScope(Main).launch {
            val data: Flow<Int> = stateProducer()
            delay(3000)
            data.collect {
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

     fun sharedProducer(): Flow<Int>{
        var mutableFlow = MutableSharedFlow<Int>(1)
        CoroutineScope(Main).launch{
        val list = listOf<Int>(1,2,3,4,5)
        list.forEach {
                mutableFlow.emit(it)
                delay(1000)
                }
        }
        return mutableFlow
    }

    fun stateProducer(): Flow<Int>{
        var mutableFlow = MutableStateFlow(10)
        CoroutineScope(IO).launch{
            delay(1000)
            mutableFlow.emit(20)
            delay(1000)
            mutableFlow.emit(50)
        }
        return mutableFlow
    }
}