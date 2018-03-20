package swan.biz.koala

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by stephen on 18-3-19.
 */
class RecyclerItemClickListener constructor (
        context: Context,
        listener: OnItemClickListener
) : RecyclerView.OnItemTouchListener {

    private var listener: OnItemClickListener? = null

    private var gestureDetector: GestureDetector? = null

    private var childView: View? = null

    private var childViewPosition: Int = 0

    init {
        this.gestureDetector = GestureDetector(context, GestureListener())
        this.listener = listener
    }

    override fun onInterceptTouchEvent(view: RecyclerView, event: MotionEvent): Boolean {
        childView = view.findChildViewUnder(event.x, event.y)
        childViewPosition = view.getChildLayoutPosition(childView)

        return childView != null && (gestureDetector?.onTouchEvent(event) ?: false)
    }

    override fun onTouchEvent(view: RecyclerView, event: MotionEvent) {
        // Not needed.
    }

    override fun onRequestDisallowInterceptTouchEvent(b: Boolean) {

    }

    /**
     * A click listener for items.
     */
    interface OnItemClickListener {

        /**
         * Called when an item is clicked.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        fun onItemClick(childView: View?, position: Int)

        /**
         * Called when an item is long pressed.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        fun onItemLongPress(childView: View?, position: Int)

    }

    /**
     * A simple click listener whose methods can be overridden one by one.
     */
    open class SimpleOnItemClickListener : OnItemClickListener {

        /**
         * Called when an item is clicked. The default implementation is a no-op.
         *
         * @param childView View of the item that was clicked.
         * @param position  Position of the item that was clicked.
         */
        override fun onItemClick(childView: View?, position: Int) {
            // Do nothing.
        }

        /**
         * Called when an item is long pressed. The default implementation is a no-op.
         *
         * @param childView View of the item that was long pressed.
         * @param position  Position of the item that was long pressed.
         */
        override fun onItemLongPress(childView: View?, position: Int) {
            // Do nothing.
        }
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return childView?.let {
                listener?.onItemClick(it, childViewPosition)
                false
            } ?: false
        }

        override fun onLongPress(event: MotionEvent) {
            childView.let {
                listener?.onItemLongPress(it, childViewPosition)
            }
        }

        override fun onDown(event: MotionEvent): Boolean {
            return false
        }
    }
}