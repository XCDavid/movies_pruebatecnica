package com.openpay.pruebatecnica.ui.common

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.openpay.pruebatecnica.ui.usecases.profile.ProfileFragment
import com.openpay.pruebatecnica.util.Constants

class RecyclerViewSwipeTouchListener(context: Context, recyclerView: RecyclerView,private val onSwipeCallback: (Constants.SwipeDirection?) -> Unit ) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
        recyclerView.addOnItemTouchListener(this)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView = rv.findChildViewUnder(e.x, e.y)
        if (childView != null && gestureDetector.onTouchEvent(e)) {
            // Detecta el swipe horizontal
            // Si no necesitas manejar el evento de swipe, simplemente devuelve 'false'.
            onSwipeCallback(null)
            return true
        }
        // Si no es un swipe horizontal, deja que el RecyclerView maneje el evento.
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
            if (Math.abs(diffX) > Math.abs(diffY)
                && Math.abs(diffX) > SWIPE_THRESHOLD
                && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
            ) {
                // Es un swipe horizontal, maneja el evento
                return true
            }
            return false
        }
    }
}