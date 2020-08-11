package com.aniview.demo.v2.ui.playback

import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aniview.demo.R
import com.aniview.demo.v2.cms.ConfigManager
import com.aniview.demo.v2.model.PlayableAsset
import com.aniview.demo.v2.model.SdkConfiguration
import com.aniview.demo.v2.model.SdkSettings
import com.aniview.demo.v2.utils.ConfigMerge
import com.aniview.demo.v2.utils.visibleOrGone
import com.aniview.exo.ExoPlayerExtension
import com.aniview.sdk.Aniview
import com.aniview.sdk.AniviewConfig
import com.aniview.sdk.AniviewSession
import com.aniview.sdk.SessionContext
import com.aniview.sdk.player.bridge.PlayerBridge
import com.aniview.sdk.player.bridge.SimplePlayerBridge
import com.aniview.sdk.player.dispatchers.AdDispatcher
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ExoActivity : AppCompatActivity() {

	private val mDisposable = CompositeDisposable()
	private var mBridge: SimplePlayerBridge? = null
	private var mSession: AniviewSession? = null
	private var mAniview: Aniview? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.a_simple_exo)

		val playerView = findViewById<PlayerView>(R.id.player)
		//container has to match the player view as its used for ad overlays
		val container = findViewById<ViewGroup>(R.id.container)
		val generalInfo = findViewById<TextView>(R.id.general_info) //debug view
		val debugInfo = findViewById<TextView>(R.id.debug_info) //debug view

		val settings = getSettings()
		val asset = intent.getParcelableExtra<PlayableAsset>("asset")
		if (asset == null) {
			finish()
			return
		}

		val config = ConfigMerge.merge(asset.config, getConfig())

		val player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
		val exo = ExoPlayerExtension(this, player)

		//configuring the UI for exo player
		playerView.player = player
		playerView.controllerAutoShow = false
		playerView.setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
		exo.setSurface((playerView?.videoSurfaceView as? SurfaceView)?.holder?.surface)

		val bridge = SimplePlayerBridge(container, exo)
		bridge.addContentCallback(object : PlayerBridge.Callback() {
			override fun onPlay() {
				playerView.useController = true
			}
		})
		mBridge = bridge

		//entry point for selecting ad configurations
		val configUrl = ConfigManager.getRemoteUrl()
		val aniview = Aniview.Builder(this, AniviewConfig(config.publisherId, config.channelId, config.configId))
				.remoteConfig(configUrl)
				.build()

		mAniview = aniview

		//initialisation of session and using the SDK url for playback
		val session = aniview.newSession(Uri.parse(asset.url), mBridge)
		mSession = session
		mDisposable.add(session.redirectUrl
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe { uri ->
					mBridge?.playContent(uri)
				})

		//as the SDK doesn't 'known' about the player view - manipulate it externally
		session.addAdCallback(object : AdDispatcher.AdCallback() {
			override fun hidePlayerControls() {
				playerView.useController = false
			}

			override fun showPlayerControls() {
				playerView.useController = true
			}
		})

		//for debug views (if enabled by config see side bar of main fragment)
		//region debug
		var stats = statsEnabled()
		if (!settings.showDebug) {
			stats = settings.debugMode
		}
		if (!stats) {
			generalInfo.visibleOrGone(false)
			debugInfo.visibleOrGone(false)
		} else {
			mDisposable.add(session.trackStats()
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe { generalInfo.text = it })

			if (session is SessionContext) {
				mDisposable.add(session.aniviewDebugStats.trackStats()
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe { debugInfo.text = it })
			}
		}
		//endregion
	}

	override fun onStart() {
		super.onStart()

		mSession?.onSessionResume()
	}

	override fun onStop() {
		super.onStop()

		mSession?.onSessionPause()
	}

	override fun onDestroy() {
		super.onDestroy()

		mDisposable.dispose()
		mAniview?.release()
		mBridge?.release()
	}

	private fun statsEnabled(): Boolean {
		return PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean("stats_enabled", false)
	}

	private fun getConfig(): SdkConfiguration? {
		return intent.getParcelableExtra<SdkConfiguration>("config")
	}

	private fun getSettings(): SdkSettings {
		return intent.getParcelableExtra<SdkSettings>("settings") ?: SdkSettings.Null
	}

}
