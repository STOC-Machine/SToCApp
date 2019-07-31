package com.example.maxnarvaez.stocapp

import android.annotation.SuppressLint
import java.io.InputStream
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView


class MyTaskParams(val instream: InputStream, val qr_field: TextView) {
//    init {
//        instream = InStream
//    }

}

class QrReceiver : AsyncTask<MyTaskParams, Void, Void>() {
//    private var socket = Socket()
//    private var serverSock = ServerSocket()
    override fun doInBackground(vararg params: MyTaskParams): Void? {
        try {
            print("Try to receive message")
            print("Try to receive message1")
            var m = Message(params[0].instream)
            params[0].qr_field.text = m.getContent()
        } catch (e: Exception) {
            Log.e("Parser Connection", "Connect failed: $e")
        }

        return null
    }

}