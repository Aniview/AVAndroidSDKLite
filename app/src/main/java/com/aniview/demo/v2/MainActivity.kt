package com.aniview.demo.v2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.aniview.demo.R
import com.aniview.demo.v2.base.BaseActivity
import com.aniview.demo.v2.ui.splash.SplashFragment
class MainActivity : BaseActivity() {

	companion object {
		const val PERMISSION = 4815
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.a_main)

		if (ContextCompat.checkSelfPermission(
						this,
						Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			resumeFlow()
		} else {
			ActivityCompat.requestPermissions(this,
					arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
					PERMISSION)
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (PERMISSION == requestCode
				&& grantResults.isNotEmpty()
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			resumeFlow()
		}
	}

	private fun resumeFlow() {
		supportFragmentManager.beginTransaction()
				.add(R.id.container, SplashFragment.newInstance())
				.commit()

		ViewModelProviders.of(this).get(MainViewModel::class.java)
	}

}
