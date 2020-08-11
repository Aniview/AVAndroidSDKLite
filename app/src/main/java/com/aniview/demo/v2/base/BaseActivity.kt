package com.aniview.demo.v2.base

import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Maksym Popovych
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.requestBackEvent()) return
        }
        return super.onBackPressed()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        supportFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.requestDispatchKeyEvent(event)) return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        supportFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.requestOnKeyDown(event)) return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        supportFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.requestOnKeyUp(event)) return true
        }
        return super.onKeyUp(keyCode, event)
    }

}