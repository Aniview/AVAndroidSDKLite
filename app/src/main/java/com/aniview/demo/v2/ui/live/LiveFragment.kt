package com.aniview.demo.v2.ui.live

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.aniview.demo.R
import com.aniview.demo.v2.MainViewModel
import com.aniview.demo.v2.base.BaseFragment
import com.aniview.demo.v2.model.SdkConfiguration
import com.aniview.demo.v2.model.SdkSettings
import com.aniview.demo.v2.utils.startPlayback
import com.aniview.demo.v2.utils.visibleOrNot
import com.aniview.demo.v2.view.LiveAdapter
import io.reactivex.disposables.Disposables

/**
 * @author Maksym Popovych
 */
class LiveFragment : BaseFragment() {

	companion object {
		fun newInstance() = LiveFragment()
	}

	private var disposable = Disposables.disposed()

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.f_live, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val model = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

		enterTransition = Slide(Gravity.START)
		exitTransition = Slide(Gravity.START)
		reenterTransition = Slide(Gravity.START)
		returnTransition = Slide(Gravity.START)

		val warning = view.findViewById<TextView>(R.id.warning)

		val recycler = view.findViewById<RecyclerView>(R.id.recycler)
		val adapter = LiveAdapter()

		recycler.adapter = adapter
		recycler.layoutManager = LinearLayoutManager(context)
		disposable.dispose()
		disposable = adapter.onClickSubject.subscribe { asset ->
			val useRemoteSettings = view.resources.getBoolean(R.bool.remote_playlist_use_settings)
			val settings = if (useRemoteSettings) model.content.value.settings ?: SdkSettings.Null else SdkSettings.ENABLED
			val complex = if (settings.showPlayerMode) {
				model.complexMode.value
			} else {
				settings.playerMode == 2
			}
			context?.startPlayback(asset, model.content.value.config ?: SdkConfiguration.Null, settings, complex)
		}

		model.content
				.observe(this, Observer { content ->
					adapter.setAll(content.live)
					warning.visibleOrNot(content.live.isEmpty())
				})
	}
}