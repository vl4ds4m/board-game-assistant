package org.vl4ds4m.board.game.assistant.network

import android.net.nsd.NsdManager

const val PROTOCOL_TYPE = NsdManager.PROTOCOL_DNS_SD
const val SERVICE_NAME = "SessionDiscovery"
const val SERVICE_TYPE = "_bga._tcp"

val Int.nsdError: String get() = when (this) {
    NsdManager.FAILURE_INTERNAL_ERROR -> "FAILURE_INTERNAL_ERROR"
    NsdManager.FAILURE_ALREADY_ACTIVE -> "FAILURE_ALREADY_ACTIVE"
    NsdManager.FAILURE_MAX_LIMIT -> "FAILURE_MAX_LIMIT"
    NsdManager.FAILURE_OPERATION_NOT_RUNNING -> "FAILURE_OPERATION_NOT_RUNNING"
    NsdManager.FAILURE_BAD_PARAMETERS -> "FAILURE_BAD_PARAMETERS"
    else -> "UNKNOWN FAILURE"
}
