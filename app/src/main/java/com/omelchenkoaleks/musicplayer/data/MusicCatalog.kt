package com.omelchenkoaleks.musicplayer.data

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.omelchenkoaleks.musicplayer.data.model.Track
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MusicCatalog(context: Context) {

    init {
        setCatalogFromJson(context)
    }

    val bitmaps = HashMap<String, Bitmap>(5)
    private var _catalog: List<Track>? = null
    private val catalog: List<Track> get() = requireNotNull(_catalog)

    val maxTrackIndex = catalog.size - 1
    var currentTrackIndex = 0
    val countTracks = catalog.size

    var currentTrack = catalog[0]
        get() = catalog[currentTrackIndex]
        private set

    fun next(): Track {
        if (currentTrackIndex == maxTrackIndex) {
            currentTrackIndex = 0
        } else {
            currentTrackIndex++
        }
        return currentTrack
    }

    fun previous(): Track {
        if (currentTrackIndex == 0) {
            currentTrackIndex = maxTrackIndex
        } else {
            currentTrackIndex--
        }
        return currentTrack
    }

    fun getTrackByIndex(index: Int) = catalog[index]

    fun getTrackCatalog() = catalog

    private fun setCatalogFromJson(context: Context) {
        val moshi = Moshi.Builder().build()
        val array = Types.newParameterizedType(List::class.java, Track::class.java)
        val adapter: JsonAdapter<List<Track>> = moshi.adapter(array)
        val file = "playlist.json"
        val json = context.assets.open(file).bufferedReader().use { it.readText() }
        _catalog = adapter.fromJson(json)

        GlobalScope.launch(Dispatchers.Default) {
            try {
                _catalog?.forEach {
                    val bitmap = Glide.with(context)
                        .asBitmap()
                        .load(it.bitmapUri)
                        .into(200, 200).get()
                    bitmaps[it.bitmapUri] = bitmap
                }

            } catch (e: Exception) {
            }
        }
    }
}