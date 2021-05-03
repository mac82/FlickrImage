package com.softinsa.myapplication.data.network.utils

import com.softinsa.myapplication.enums.StatusNetworkCall
import com.softinsa.myapplication.enums.StatusNetworkCall.SUCCESS;
import com.softinsa.myapplication.enums.StatusNetworkCall.ERROR;
import com.softinsa.myapplication.enums.StatusNetworkCall.LOADING;

data class ResourceNetworkCall<out T>(val status: StatusNetworkCall, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): ResourceNetworkCall<T> = ResourceNetworkCall(status = SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): ResourceNetworkCall<T> =
            ResourceNetworkCall(status = ERROR, data = data, message = message)

        fun <T> loading(data: T?): ResourceNetworkCall<T> = ResourceNetworkCall(status = LOADING, data = data, message = null)
    }
}