package com.examples.coding.multithreadingdemo

import android.os.AsyncTask
import android.util.Log
import org.greenrobot.eventbus.EventBus
import java.lang.StringBuilder

class AsyncTaskDemo(val asyncTaskCallback: AsyncTaskCallback) : AsyncTask<String, String, String>() {
    //Runs on Main Thread
    //Setup the task to run
    val stringToReverse = "I really wish it was Friday"
    lateinit var reversedString : String
    override fun onPreExecute() {
        super.onPreExecute()
        reversedString = StringBuilder().append(stringToReverse).reverse().toString()
        Log.d("ASYNC", reversedString)
    }
    //Runs on WORKER thread
    //The task that needs to be run
    override fun doInBackground(vararg p0: String?): String {
        var stringFixed = ""
        for(i in 0 .. reversedString.length) {
            stringFixed = "${reversedString[i]}${stringFixed}"
            publishProgress(stringFixed)
        }
        return stringFixed
    }
    //Runs on Main Thread
    //Used to get updates about task status
    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
        Log.d("ASYNC", values[0])
    }
    //Runs on Main Thread
    // Reports the results of the task
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d("ASYNC", result)
        //asyncTaskCallback.onResult(result)
        EventBus.getDefault().post(AsynTaskEvent(result?: "No Result"))
    }
}

interface AsyncTaskCallback {
    fun onResult(result: String?)
}