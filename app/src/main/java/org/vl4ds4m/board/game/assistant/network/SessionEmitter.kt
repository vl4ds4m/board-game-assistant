package org.vl4ds4m.board.game.assistant.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import org.vl4ds4m.board.game.assistant.game.GameType

class SessionEmitter(
    private val nsdManager: NsdManager,
    private val observer: SessionObserver
) {
    private val listener = object : NsdManager.RegistrationListener {
        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.w(TAG, "Service registration failed: ${errorCode.nsdError}")
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.w(TAG, "Service logout failed: ${errorCode.nsdError}")
            observer.ownServiceName = null
        }

        override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
            Log.i(TAG, "Service registered: $serviceInfo")
            observer.ownServiceName = serviceInfo?.serviceName
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
            Log.i(TAG, "Service logout")
            observer.ownServiceName = null
        }
    }

    fun register(id: String, type: GameType, name: String, port: Int) {
        NsdServiceInfo().apply {
            serviceType = SERVICE_TYPE
            serviceName = SERVICE_NAME
            this.port = port
            setAttribute(RemoteSessionInfo.TXT_ID, id)
            setAttribute(RemoteSessionInfo.TXT_TYPE, type.title)
            setAttribute(RemoteSessionInfo.TXT_NAME, name)
        }.let {
            nsdManager.registerService(it, PROTOCOL_TYPE, listener)
        }
    }

    fun unregister() {
        nsdManager.unregisterService(listener)
    }
}

private const val TAG = "SessionEmitter"
