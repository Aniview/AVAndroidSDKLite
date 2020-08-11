package com.aniview.demo.v2.ui

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aniview.demo.R
import com.aniview.demo.v2.MainViewModel
import com.aniview.demo.v2.base.BaseFragment
import com.aniview.demo.v2.model.SdkSettings
import com.aniview.demo.v2.ui.live.LiveFragment
import com.aniview.demo.v2.ui.vod.VodFragment
import com.aniview.demo.v2.utils.visibleOrGone

/**
 * @author Maksym Popovych
 */
class MainFragment : BaseFragment(), Observer<MainState> {
	companion object {
		fun newInstance() = MainFragment()
	}

	private val model by lazy { ViewModelProviders.of(activity!!).get(MainViewModel::class.java) }
	private var drawer: DrawerLayout? = null
	private var live: TextView? = null
	private var vod: TextView? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.f_main, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val useRemoteSettings = view.resources.getBoolean(R.bool.remote_playlist_use_settings)
		val settings = if (useRemoteSettings) model.content.value.settings ?: SdkSettings.Null else SdkSettings.ENABLED

		vod = view.findViewById<TextView>(R.id.vod)
		vod?.setOnClickListener {
			model.setState(MainState.Vod)
		}

		live = view.findViewById<TextView>(R.id.live)
		live?.setOnClickListener {
			model.setState(MainState.Live)
		}

		drawer = view.findViewById<DrawerLayout>(R.id.drawer_layout)
		view.findViewById<View>(R.id.menu).setOnClickListener {
			drawer?.openDrawer(GravityCompat.START)
		}

		val statsSwitch = view.findViewById<Switch>(R.id.stats_switch)
		statsSwitch.setOnCheckedChangeListener { _, state ->
			model.setStatsEnabled(state)
		}

		val complexModeSwitch = view.findViewById<Switch>(R.id.complex_mode)
		complexModeSwitch.setOnCheckedChangeListener { _, state ->
			model.setComplexMode(state)
		}

		statsSwitch.visibleOrGone(settings.showDebug)
		complexModeSwitch.visibleOrGone(settings.showPlayerMode)

		model.trackComplexMode(this, Observer { state -> complexModeSwitch.isChecked = state })
		model.trackStatsEnabled(this, Observer { state -> statsSwitch.isChecked = state })

		model.state.observe(this, this)
	}

	override fun onChanged(new: MainState) {
		val existing = childFragmentManager.findFragmentByTag(new.name)

		val fragment = existing ?: when (new) {
			MainState.Vod -> VodFragment.newInstance()
			MainState.Live -> LiveFragment.newInstance()
		}

		childFragmentManager.beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.replace(R.id.content, fragment, new.name)
				.addToBackStack(null)
				.commit()

		val liveView = live ?: return
		val vodView = vod ?: return

		when (new) {
			MainState.Vod -> {
				VodFragment.newInstance()
				liveView.typeface = Typeface.DEFAULT
				vodView.typeface = Typeface.DEFAULT_BOLD
				vodView.paintFlags = vodView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
				liveView.paintFlags = liveView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
			}
			MainState.Live -> {
				LiveFragment.newInstance()
				vodView.typeface = Typeface.DEFAULT
				liveView.typeface = Typeface.DEFAULT_BOLD
				liveView.paintFlags = liveView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
				vodView.paintFlags = vodView.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
			}
		}
	}

	override fun onBackPressed(): Boolean {
		val drawerRef = drawer
		if (drawerRef?.isDrawerOpen(GravityCompat.START) == true) {
			drawerRef.closeDrawers()
			return true
		}

		return super.onBackPressed()
	}
}