package com.aniview.demo.v2.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aniview.demo.R
import com.aniview.demo.v2.model.PlayableAsset
import com.aniview.demo.v2.utils.visibleOrGone
import com.bumptech.glide.Glide

/**
 * @author Maksym Popovych
 */
class LiveAdapter : AbstractAdapter<LiveAdapter.Holder, PlayableAsset>(LiveFactory()) {

	abstract class Holder(itemView: View) : AbstractAdapter.AbstractVH<PlayableAsset>(itemView)

	companion object {
		private const val PROMOTED = 1
		private const val BASIC = 0
	}

	override fun getItemViewType(item: PlayableAsset): Int? {
		return if (item.promoted) {
			PROMOTED
		} else {
			BASIC
		}
	}

	class LiveHolder(itemView: View) : Holder(itemView) {
		private var description: TextView = itemView.findViewById(R.id.description)
		private var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
		private var title: TextView = itemView.findViewById(R.id.title)
		private var asset: PlayableAsset? = null

		override fun onBind(item: PlayableAsset) {
			asset = item

			Glide.with(thumbnail.context)
					.load(item.thumbnail)
					.into(thumbnail)

			description.text = item.description
			description.visibleOrGone(item.description.isNotBlank())

			title.text = item.title
			title.visibleOrGone(item.title.isNotBlank())
		}
	}

	class LiveFactory : AbstractFactory<Holder, PlayableAsset>() {
		override fun createViewHolder(parent: ViewGroup, viewType: Int): LiveHolder {
			val layout = if (viewType == PROMOTED) {
				R.layout.vh_live_promo
			} else {
				R.layout.vh_live_basic
			}
			val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
			return LiveHolder(view)
		}
	}

}