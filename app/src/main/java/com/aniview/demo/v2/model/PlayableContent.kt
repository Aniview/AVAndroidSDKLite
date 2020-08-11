package com.aniview.demo.v2.model

import com.google.gson.annotations.SerializedName

/**
 * @author Maksym Popovych
 */
data class PlayableContent(
		@SerializedName("vod")
		val vod: List<PlayableAsset>,
		@SerializedName("live")
		val live: List<PlayableAsset>,
		@SerializedName("config")
		val config: SdkConfiguration?,
		@SerializedName("settings")
		val settings: SdkSettings? = SdkSettings.Null
) {
	companion object {
		val Null = PlayableContent(emptyList(), emptyList(), SdkConfiguration.Null, SdkSettings.Null)
	}

}