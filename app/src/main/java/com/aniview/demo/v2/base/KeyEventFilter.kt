package com.aniview.demo.v2.base

import android.util.SparseIntArray
import android.view.KeyEvent

/**
 * @author Maksym Popovych
 */

class KeyEventFilter {

    private val timestamps = SparseIntArray()

    fun verify(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
            timestamps.put(event.keyCode, event.keyCode)
            return true
        }
        if (timestamps.indexOfKey(event.keyCode) < 0) {
            return false
        }
        if (event.action == KeyEvent.ACTION_UP) {
            timestamps.delete(event.keyCode)
        }
        return true
    }

}