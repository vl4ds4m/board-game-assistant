package org.vl4ds4m.board.game.assistant.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.vl4ds4m.board.game.assistant.game.GameType

class SessionEmitter(
    private val nsdManager: NsdManager,
    private val sessionObserver: SessionObserver
) {
    private val listener = object : NsdManager.RegistrationListener {
        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.w(TAG, "Service registration failed: ${errorCode.nsdError}")
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.w(TAG, "Service logout failed: ${errorCode.nsdError}")
            sessionObserver.ownServiceName = null
        }

        override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
            Log.i(TAG, "Service registered: $serviceInfo")
            sessionObserver.ownServiceName = serviceInfo?.serviceName
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
            Log.i(TAG, "Service logout")
            sessionObserver.ownServiceName = null
        }
    }

    private var serviceInfo: NsdServiceInfo? = null

    val enabled: Boolean
        get() = serviceInfo != null

    fun enable(id: String, type: GameType, name: String, port: Int) {
        serviceInfo = NsdServiceInfo().apply {
            serviceType = SERVICE_TYPE
            serviceName = SERVICE_NAME
            this.port = port
            setAttribute(RemoteSessionInfo.TXT_ID, id)
            setAttribute(RemoteSessionInfo.TXT_TYPE, type.title)
            setAttribute(RemoteSessionInfo.TXT_NAME, name)
        }
        register()
    }

    private fun register() {
        if (enabled) {
            nsdManager.registerService(serviceInfo, PROTOCOL_TYPE, listener)
        }
    }

    fun disable() {
        unregister()
        serviceInfo = null
    }

    private fun unregister() {
        if (enabled) {
            nsdManager.unregisterService(listener)
        }
    }

    val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            register()
        }

        override fun onStop(owner: LifecycleOwner) {
            unregister()
        }
    }
}

private const val TAG = "SessionEmitter"
