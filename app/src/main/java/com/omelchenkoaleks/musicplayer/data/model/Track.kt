package com.omelchenkoaleks.musicplayer.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Track(
    val title: String,
    val artist: String,
    val bitmapUri: String,
    val trackUri: String,
    val duration: Long
)