package com.aniview.demo.v2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym Popovych
 */
@Parcelize
data class SdkConfiguration(
		@SerializedName("publisherId")
		val publisherId: String?,
		@SerializedName("channelId")
		val channelId: String?,
		@SerializedName("configId")
		val configId: String?) : Parcelable {

	companion object {
		val Null = SdkConfiguration("5d53c40328a0612a9c2b9afd", "5d75ff6e28a0612f10609766", "")
	}

}