package org.vl4ds4m.board.game.assistant.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log

class SessionEmitter(private val nsdManager: NsdManager) {
    fun register(port: Int) {
        NsdServiceInfo().apply {
            serviceType = SERVICE_TYPE
            serviceName = SERVICE_NAME
            this.port = port
        }.let {
            nsdManager.registerService(it, PROTOCOL_TYPE, listener)
        }
    }

    fun unregister() {
        nsdManager.unregisterService(listener)
    }
}

private const val TAG = "SessionEmitter"

private val listener = object : NsdManager.RegistrationListener {
    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        Log.w(TAG, "Service registration failed: ${errorCode.nsdError}")
    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        Log.w(TAG, "Service logout failed: ${errorCode.nsdError}")
    }

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
        Log.i(TAG, "Service registered")
    }

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
        Log.i(TAG, "Service logout")
    }
}
