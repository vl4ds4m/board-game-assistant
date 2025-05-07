package org.vl4ds4m.board.game.assistant.network

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.vl4ds4m.board.game.assistant.game.GameType
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo.Companion.TXT_ID
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo.Companion.TXT_NAME
import org.vl4ds4m.board.game.assistant.network.RemoteSessionInfo.Companion.TXT_TYPE
import org.vl4ds4m.board.game.assistant.updateMap

class SessionObserver(private val nsdManager: NsdManager) {
    private val mSessions = MutableStateFlow<Map<String, RemoteSessionInfo>>(mapOf())
    val sessions: StateFlow<Map<String, RemoteSessionInfo>> = mSessions.asStateFlow()

    var ownServiceName: String? = null

    private val discoveryListener = object : NsdManager.DiscoveryListener {
        override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.w(TAG, "Discovery start failed: ${errorCode.nsdError}")
        }

        override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.w(TAG, "Discovery stop failed: ${errorCode.nsdError}")
            mSessions.value = mapOf()
        }

        override fun onDiscoveryStarted(serviceType: String?) {
            Log.i(TAG, "Discovery started")
        }

        override fun onDiscoveryStopped(serviceType: String?) {
            Log.i(TAG, "Discovery stopped")
            mSessions.value = mapOf()
        }

        override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
            Log.i(TAG, "Service found: " +
                serviceInfo?.run { "$serviceName ($serviceType)" })
            val name = serviceInfo?.serviceName ?: return
            val type = serviceInfo.serviceType
            if (type.contains(SERVICE_TYPE) && name.contains(SERVICE_NAME)) {
                Log.i(TAG, "Service '$name' is a game session invitation")
                nsdManager.resolveService(serviceInfo, resolveListener)
            }
        }

        override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
            Log.i(TAG, "Service lost: ${serviceInfo?.serviceName}")
            serviceInfo?.serviceName?.let {
                mSessions.updateMap { remove(it) }
            }
        }
    }

    private val resolveListener get() = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.w(TAG, "Service resolution failed: ${errorCode.nsdError}")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
            Log.i(TAG, "Service resolved: $serviceInfo")
            serviceInfo?.toRemoteSession?.let {
                if (serviceInfo.serviceName == ownServiceName) return
                mSessions.updateMap {
                    putIfAbsent(serviceInfo.serviceName, it)
                }
            }
        }
    }

    fun startDiscovery() {
        nsdManager.discoverServices(SERVICE_TYPE, PROTOCOL_TYPE, discoveryListener)
    }

    fun stopDiscovery() {
        nsdManager.stopServiceDiscovery(discoveryListener)
    }
}

private const val TAG = "SessionObserver"

private val NsdServiceInfo.toRemoteSession: RemoteSessionInfo? get() {
    val id = attributes[TXT_ID]
        ?.let { String(it) } ?: return null
    val type = attributes[TXT_TYPE]
        ?.let { String(it) } ?: return null
    val name = attributes[TXT_NAME]
        ?.let { String(it) } ?: return null
    return RemoteSessionInfo(
        id = id,
        name = name,
        type = GameType.valueOf(type),
        ip = host.canonicalHostName,
        port = port
    )
}
