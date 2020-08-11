package com.aniview.demo.v2

import android.app.Application
import android.preference.PreferenceManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.aniview.demo.v2.cms.ContentManager
import com.aniview.demo.v2.model.PlayableContent
import com.aniview.demo.v2.ui.MainState
import com.aniview.demo.v2.utils.SafeLiveData
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

/**
 * @author Maksym Popovych
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

	companion object {
		private const val TAG = "MainViewModel"

		const val COMPLEX_MODE = "complex_mode"
		const val STATS_ENABLED = "stats_enabled"
	}

	private val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication<Application>())

	val state = SafeLiveData(MainState.Vod)
	val content = SafeLiveData(PlayableContent.Null)
	val complexMode = SafeLiveData(prefs.getBoolean(COMPLEX_MODE, true))

	private val statsEnabled = SafeLiveData(prefs.getBoolean(STATS_ENABLED, false))
	private var disposable = Disposables.disposed()

	init {
		fetchContent()
	}

	fun setState(new: MainState) {
		state.postValue(new)
	}

	fun setStatsEnabled(enabled: Boolean) {
		if (enabled == statsEnabled.value) {
			return
		}

		statsEnabled.postValue(enabled)

		prefs.edit().putBoolean(STATS_ENABLED, enabled).apply()
	}

	fun trackComplexMode(owner: LifecycleOwner, block: Observer<Boolean>) {
		complexMode.observe(owner, block)
	}

	fun setComplexMode(enabled: Boolean) {
		if (enabled == complexMode.value) {
			return
		}

		complexMode.postValue(enabled)

		prefs.edit().putBoolean(COMPLEX_MODE, enabled).apply()
	}

	fun trackStatsEnabled(owner: LifecycleOwner, block: Observer<Boolean>) {
		statsEnabled.observe(owner, block)
	}

	private fun fetchContent() {
		Log.d(TAG, "fetching content")
		disposable.dispose()
		disposable = ContentManager.fetchContent(getApplication(), false).subscribeOn(Schedulers.io())
				.subscribe { cont ->
					content.postValue(cont)
				}
	}

	override fun onCleared() {
		super.onCleared()

		disposable.dispose()
	}
}