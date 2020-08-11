package com.aniview.demo.v2.utils

import com.aniview.demo.v2.model.SdkConfiguration

/**
 * @author Maksym Popovych
 */
object ConfigMerge {

	fun merge(left: SdkConfiguration?, right: SdkConfiguration?): SdkConfiguration {
		return SdkConfiguration(
				left?.publisherId ?: right?.publisherId ?: SdkConfiguration.Null.publisherId,
				left?.channelId ?: right?.channelId ?: SdkConfiguration.Null.channelId,
				left?.configId ?: right?.configId ?: SdkConfiguration.Null.configId
		)
	}

}