package com.aniview.demo.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym Popovych
 */
@Parcelize
data class PlayableAsset(
		@SerializedName("title")
		val title: String,
		@SerializedName("description")
		val description: String,
		@SerializedName("url")
		val url: String,
		@SerializedName("rating")
		val rating: Float,
		@SerializedName("thumbnail")
		val thumbnail: String,
		@SerializedName("promoted")
		val promoted: Boolean = false,
		@SerializedName("sdkConfig")
		val config: SdkConfiguration?) : Parcelable {

	companion object {
		val Null = PlayableAsset("", "", "", 0f, "", false, SdkConfiguration.Null)
	}

}