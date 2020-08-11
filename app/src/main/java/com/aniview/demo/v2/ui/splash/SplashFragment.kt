package com.aniview.demo.v2.ui.splash

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Slide
import com.aniview.demo.R
import com.aniview.demo.v2.MainViewModel
import com.aniview.demo.v2.base.BaseFragment
import com.aniview.demo.v2.model.PlayableContent
import com.aniview.demo.v2.ui.MainFragment

/**
 * @author Maksym Popovych
 */
class SplashFragment : BaseFragment() {

	companion object {
		fun newInstance() = SplashFragment()
	}

	private val model by lazy { ViewModelProviders.of(activity!!).get(MainViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.f_splash, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		exitTransition = Slide(Gravity.TOP)

		model.content.observe(this, Observer { content ->
			if (content != PlayableContent.Null) {
				nextFragment()
			}
		})
	}

	private fun nextFragment() {
		val manager = fragmentManager ?: return
		manager.beginTransaction()
				.replace(this.id, MainFragment.newInstance())
				.commit()
	}
}