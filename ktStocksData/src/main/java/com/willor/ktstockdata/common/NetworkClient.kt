package com.willor.ktstockdata.common

import okhttp3.OkHttpClient

internal class NetworkClient {


    companion object {

        private var client: OkHttpClient? = null


        fun getClient(): OkHttpClient {
            if (client == null) {
                client = buildClient()
            }

            return client!!
        }


        private fun buildClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .followRedirects(true)      // VERY IMPORTANT FOR ANDROID
                .followSslRedirects(true)       // ALSO MAYBE
                .build()
        }


    }
}