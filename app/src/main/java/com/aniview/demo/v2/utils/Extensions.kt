package com.aniview.demo.v2.utils

import android.content.Context
import android.content.Intent
import android.view.View
import com.aniview.demo.v2.model.PlayableAsset
import com.aniview.demo.v2.model.SdkConfiguration
import com.aniview.demo.v2.model.SdkSettings
import com.aniview.demo.v2.ui.playback.*

/**
 * @author Maksym Popovych
 */

fun View.visibleOrGone(visible: Boolean) {
	this.visibility = if (visible) {
		View.VISIBLE
	} else {
		View.GONE
	}
}

fun View.visibleOrNot(visible: Boolean) {
	this.visibility = if (visible) {
		View.VISIBLE
	} else {
		View.INVISIBLE
	}
}
fun Context.startPlayback(asset: PlayableAsset, config: SdkConfiguration, settings: SdkSettings, complex: Boolean) {
	val intent = if (complex) {
		Intent(this, ComplexExoActivity::class.java)
	} else {
		Intent(this, ExoActivity::class.java)
	}

	intent.putExtra("asset", asset)
	intent.putExtra("settings", settings)
	intent.putExtra("config", config)
	this.startActivity(intent)
}