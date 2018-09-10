package com.teambition.task.ganttchart

import android.content.Context
import android.graphics.Canvas
import android.support.v4.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller

class RenderEngine(val context: Context, val view: View, var tasks: List<GanttTaskList>,
                   val timeSpanGenerator: TimeSpanGenerator, val taskClickListener: TaskClickListener):
    GestureDetector.SimpleOnGestureListener() {

    var eps = 0.0f
    val style: Style
    val drawer: ElementDrawer
    val touchDetector: GestureDetectorCompat
    val myScroller = Scroller(context)

    interface TaskClickListener {
        fun onTaskClick(id: String?)
    }

    init {
        style = Style(context.resources.displayMetrics.density)
        drawer = ElementDrawer(tasks, style, timeSpanGenerator)
        touchDetector = GestureDetectorCompat(context, this)
    }

    fun render(canvas: Canvas) {
        if (myScroller.computeScrollOffset()) {
            eps = myScroller.currX.toFloat()
        }
        drawer.draw(canvas, eps)
        if (myScroller.computeScrollOffset()) {
            view.invalidate()
        }
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        return touchDetector.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        myScroller.forceFinished(true)
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        myScroller.fling(eps.toInt(), 0, -velocityX.toInt(), 0, 0, drawer.epsLimit.toInt(), 0, 1)

        view.invalidate()

        return super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        eps += distanceX
        if (eps < 0.0f) {
            eps = 0.0f
        } else if (eps > drawer.epsLimit) {
            eps = drawer.epsLimit
        }
        view.invalidate()
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        if (e == null) {
            return super.onSingleTapConfirmed(e)
        }
        taskClickListener.onTaskClick(drawer.findTask(e.x + eps, e.y))
        return super.onSingleTapConfirmed(e)
    }

}