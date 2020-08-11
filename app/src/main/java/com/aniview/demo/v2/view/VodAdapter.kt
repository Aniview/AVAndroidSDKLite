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
class VodAdapter : AbstractAdapter<VodAdapter.VodHolder, PlayableAsset>(VodFactory()) {

	class VodHolder(itemView: View) : AbstractAdapter.AbstractVH<PlayableAsset>(itemView) {
		private var description: TextView = itemView.findViewById(R.id.description)
		private var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
		private var rating: RatingBar = itemView.findViewById(R.id.rating)
		private var title: TextView = itemView.findViewById(R.id.title)
		private var asset: PlayableAsset? = null

		override fun onBind(item: PlayableAsset) {
			asset = item

			Glide.with(thumbnail.context)
					.load(item.thumbnail)
					.into(thumbnail)

			title.text = item.title
			title.visibleOrGone(item.title.isNotBlank())

			rating.rating = item.rating
			rating.visibleOrGone(item.rating != 0f)

			description.text = item.description
			description.visibleOrGone(item.description.isNotBlank())
		}
	}

	class VodFactory : AbstractFactory<VodHolder, PlayableAsset>() {
		override fun createViewHolder(parent: ViewGroup, viewType: Int): VodHolder {
			val view = LayoutInflater.from(parent.context).inflate(R.layout.vh_vod_basic, parent, false)
			return VodHolder(view)
		}
	}

}