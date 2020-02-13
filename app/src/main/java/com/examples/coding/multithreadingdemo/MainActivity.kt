package com.examples.coding.multithreadingdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception

class MainActivity : AppCompatActivity(), AsyncTaskCallback {
    var isThreadOne = true
    lateinit var someString : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            tvAsyncTaskResult.text = someString
        } catch (e : Exception) {
            Log.w("ERROR", "An Error has occurred", e)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAsyncTaskEvent(event : AsynTaskEvent) {
        tvAsyncTaskResult.text = event.resultMessage
    }

    fun runTreadRunnable() {
        val thread = Thread(Runnable {
            Log.d("THREAD", "Thread Started")
            runOnUiThread{ tvThreadDisplay.text = "Thread Started"}
            for(i in 0..30) {
                Log.d("THREAD", "Thread has run for $i Seconds")
                runOnUiThread{ tvThreadDisplay.text = "Thread has run for $i Seconds"}

                Thread.sleep(1000)
            }
            runOnUiThread{ tvThreadDisplay.text ="Thread Stopped"}
        })
        Log.d("THREAD", "Thread Stopped")

        thread.start()
    }

    fun runTreadRunnableTwo() {
        val thread = Thread(Runnable {
            Log.d("THREAD", "Thread Started")
            runOnUiThread{ tvThreadDisplayTwo.text = "Thread Started"}
            for(i in 0..30) {
                Log.d("THREAD", "Thread has run for $i Seconds")
                runOnUiThread{ tvThreadDisplayTwo.text = "Thread has run for $i Seconds"}

                Thread.sleep(1000)
            }
            runOnUiThread{ tvThreadDisplayTwo.text ="Thread Stopped"}
        })
        Log.d("THREAD", "Thread Stopped")
        thread.start()
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.btnStartThreadRunnable -> {
                if(isThreadOne) {
                    runTreadRunnable()
                } else {
                    runTreadRunnableTwo()
                }
                isThreadOne = !isThreadOne
            }
            R.id.btnStartAsyncTask -> {
                val asyncTaskDemo = AsyncTaskDemo(this)
                asyncTaskDemo.execute()
            }
        }
    }

    override fun onResult(result: String?) {
        tvAsyncTaskResult.text = result?: "No Result"
    }
}
