package com.aniview.demo.v2.base

import android.view.KeyEvent
import androidx.fragment.app.Fragment

/**
 * @author Maksym Popovych
 */
abstract class BaseFragment : Fragment() {

    private val normalVerifier = KeyEventFilter()
    private val dispatchVerifier = KeyEventFilter()

    open fun onBackPressed() = false

    protected open fun dispatchBackRequested(): Boolean {
        childFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.onBackPressed()) return true
        }
        return onBackPressed()
    }

    fun requestBackEvent(): Boolean {
        return dispatchBackRequested()
    }

    protected open fun dispatchKeyEventRequested(event: KeyEvent): Boolean {
        childFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.requestDispatchKeyEvent(event)) return true
        }
        return false
    }

    fun requestDispatchKeyEvent(event: KeyEvent): Boolean {
        return dispatchVerifier.verify(event) && dispatchKeyEventRequested(event)
    }

    protected open fun onKeyDownRequested(event: KeyEvent) : Boolean {
        childFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.requestOnKeyDown(event)) return true
        }
        return false
    }

    fun requestOnKeyDown(event: KeyEvent): Boolean {
        return normalVerifier.verify(event) && onKeyDownRequested(event)
    }

    protected open fun onKeyUpRequested(event: KeyEvent): Boolean {
        childFragmentManager.fragments.forEach {
            if (it.isResumed && it is BaseFragment && it.requestOnKeyUp(event)) return true
        }
        return false
    }

    fun requestOnKeyUp(event: KeyEvent): Boolean {
        return normalVerifier.verify(event) && onKeyUpRequested(event)
    }

}