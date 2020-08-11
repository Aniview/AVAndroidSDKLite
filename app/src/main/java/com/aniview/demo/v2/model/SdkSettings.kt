package com.aniview.demo.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym Popovych
 */
@Parcelize
data class SdkSettings(
		@SerializedName("showDebug")
		val showDebug: Boolean,
		@SerializedName("debugMode")
		val debugMode: Boolean,
		@SerializedName("showPlayerMode")
		val showPlayerMode: Boolean,
		@SerializedName("playerMode")
		val playerMode: Int,
		@SerializedName("showOfflineMode")
		val showOfflineMode: Boolean
) : Parcelable {

	companion object {
		val Null = SdkSettings(showDebug = false,
				debugMode = false,
				showPlayerMode = false,
				playerMode = 2,
				showOfflineMode = false)

		val ENABLED = SdkSettings(showDebug = true,
				debugMode = false,
				showPlayerMode = true,
				playerMode = 2,
				showOfflineMode = false)
	}

}