package com.example.april19ailatrieuphu

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MediaManager {
    const val PLAYING = 1
    const val STOP = 0
    var currentState = 0

    public var mMediaPlayer = MediaPlayer()
    var mContext: Context? = null
    var mResource: Int? = null


    fun setContext(context: Context) {
        mContext = context
    }

    fun setResource(resource: Int) {
        mResource = resource
    }

    fun start(context: Context, resource: Int) {
        mResource = resource
        mContext = context
        if (currentState == STOP) {
            if (mContext != null && mResource != null) {
                currentState = PLAYING
                mMediaPlayer = MediaPlayer.create(mContext, resource)
                mMediaPlayer.start()
            }
        } else if (currentState == PLAYING) {
            mMediaPlayer.stop()
            currentState = PLAYING
            mMediaPlayer = MediaPlayer.create(mContext, resource)
            mMediaPlayer.start()

        }
    }

    fun startToEnd(context: Context, listResourse: ArrayList<Int>) {
        var current = 0
//        mMediaPlayer = MediaPlayer.create(mContext, listResourse[current])
        val a = "android.resource://" + context.packageName + "/" + listResourse[current]
        mMediaPlayer.setDataSource(
            context,
            Uri.parse("android.resource://" + context.packageName + "/" + listResourse[current])
        )
        mMediaPlayer.prepare()
        mMediaPlayer.start()
        current++
        mMediaPlayer.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            it.setDataSource(
                context,
                Uri.parse("android.resource://" + context.packageName + "/" + listResourse[current])
            )
            it.prepare()
            it.start()
        }
        )
    }

    fun stop() {
        mMediaPlayer.stop()
        currentState = STOP
    }
}