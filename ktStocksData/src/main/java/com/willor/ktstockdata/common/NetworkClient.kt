package com.willor.ktstockdata.common

import okhttp3.OkHttpClient
import okhttp3.Request

internal class NetworkClient {





    companion object {

        private var client: OkHttpClient? = null


        fun getClient(): OkHttpClient {
            if (client == null) {
                client = buildClient()
            }

            return client!!
        }


        fun getWebpage(url: String): String?{

            val call = getClient().newCall(
                Request.Builder()
                    .url(url)
                    .header("user-agent", getRandomUserAgent())
                    .get()
                    .build()
            )

            val resp = call.execute()

            if (!resp.isSuccessful){
                return null
            }

            return resp.body!!.string()
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

fun main() {
}