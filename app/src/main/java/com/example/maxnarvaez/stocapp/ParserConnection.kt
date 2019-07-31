package com.example.maxnarvaez.stocapp

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket


object ParserConnection {

    private var socket = Socket()
    private var serverSock = ServerSocket()
    private var inSock = Socket()
    private lateinit var oStream: OutputStream
    private var connected = false
    private var receiving = false
    val isConnected: Boolean
        get() = connected
    val isReceiving: Boolean
        get() = receiving

    suspend fun connect(qrResult: TextView): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                socket = Socket()
                socket.connect(InetSocketAddress(parserIP, parserPort), 5000)
                connected = true
                oStream = socket.getOutputStream()
                var tmp = MyTaskParams(socket.getInputStream(), qrResult)
                QrReceiver().execute(tmp)
            }
            true
        } catch (e: Exception) {
            Log.e("Parser Connection", "Connect failed: $e")
            connected = false
            false
        }
    }

    suspend fun send(message: String) {
        if (!connected) {
            Log.e("Parser Connection", "Not Connected")
            return
        }
        withContext(Dispatchers.IO) {
            try {
                Message("EOT", message).send(oStream)
            } catch (e: Exception) {
            }
        }
    }

    suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            socket.close()
        }
    }
}

