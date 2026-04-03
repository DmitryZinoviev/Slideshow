package com.da.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiFactory {

    fun create(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
}