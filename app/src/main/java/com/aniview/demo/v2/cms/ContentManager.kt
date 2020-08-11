package com.aniview.demo.v2.cms

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.aniview.demo.R
import com.aniview.demo.v2.model.PlayableContent
import com.google.gson.GsonBuilder
import io.reactivex.Maybe
import okhttp3.*
import java.io.IOException

/**
 * @author Maksym Popovych
 */
object ContentManager {

	private const val TAG = "ContentManager"

	private const val LOCAL_URL = "http://localhost:7339/content/"
	private val httpClient = OkHttpClient.Builder().build()
	private val gson = GsonBuilder().setLenient().create()

	fun fetchContent(context: Context, offline: Boolean = false): Maybe<PlayableContent> {
		if (offline) return fetchFromLocalServer()

		return fetchFromRemote(context)
				.switchIfEmpty(fetchFromLocalServer())
	}

	private fun fetchFromLocalServer(): Maybe<PlayableContent> {
		return Maybe.create { emitter ->
			if (TextUtils.isEmpty(LOCAL_URL)) {
				emitter.onComplete()
				return@create
			}

			val request = Request.Builder().url(LOCAL_URL).build()
			httpClient.newCall(request).enqueue(object : Callback {
				override fun onResponse(call: Call, response: Response) {
					val body = response.body()
					if (body == null) {
						emitter.onComplete()
						return
					}
					try {
						val content = gson.fromJson(body.string(), PlayableContent::class.java)
						emitter.onSuccess(content ?: return emitter.onComplete())
					} catch (e: Exception) {
						emitter.onComplete()
					}
				}

				override fun onFailure(call: Call, e: IOException) {
					emitter.onComplete()
				}
			})
		}
	}

	private fun fetchFromRemote(context: Context): Maybe<PlayableContent> {
		val remoteUrl = context.resources.getString(R.string.remote_playlist_url)

		return Maybe.create { emitter ->
			if (TextUtils.isEmpty(remoteUrl)) {
				emitter.onComplete()
				return@create
			}

			val request = Request.Builder().url(remoteUrl).build()
			httpClient.newCall(request).enqueue(object : Callback {
				override fun onResponse(call: Call, response: Response) {
					val body = response.body()
					if (body == null) {
						Log.e(TAG, "failed to fetch with response code: ${response.code()}")
						emitter.onComplete()
						return
					}
					try {
						val content = gson.fromJson(body.string(), PlayableContent::class.java)
						emitter.onSuccess(content ?: return emitter.onComplete())
					} catch (e: Exception) {
						Log.e(TAG, "failed to parse", e)
						emitter.onComplete()
					}
				}

				override fun onFailure(call: Call, e: IOException) {
					emitter.onComplete()
				}
			})
		}
	}

}