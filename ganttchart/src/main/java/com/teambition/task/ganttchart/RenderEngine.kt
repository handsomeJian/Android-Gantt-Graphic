package com.teambition.task.ganttchart

import android.content.Context
import android.graphics.Canvas
import android.support.v4.view.GestureDetectorCompat
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import java.lang.Math.abs

class RenderEngine(val context: Context, val view: View, var tasks: List<GanttTaskList>,
                   val timeSpanGenerator: TimeSpanGenerator, val taskClickListener: TaskClickListener):
    GestureDetector.SimpleOnGestureListener() {

    var epsx = 0.0f
    var epsy = 0.0f
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
            epsx = myScroller.currX.toFloat()
            epsy = myScroller.currY.toFloat()
        }
        drawer.draw(canvas, epsx, epsy)
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

        myScroller.fling(epsx.toInt(), epsy.toInt(), -velocityX.toInt(), -velocityY.toInt(),
        0, drawer.epsxLimit.toInt(), 0, drawer.epsyLimit.toInt())

        view.invalidate()

        return super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        epsx += distanceX
        epsy += distanceY
        Log.v("HERE", "distancey = $distanceY   epsy = $epsy   limit = ${drawer.epsyLimit}")
        if (epsx < 0.0f) {
            epsx = 0.0f
        } else if (epsx > drawer.epsxLimit) {
            epsx = drawer.epsxLimit
        }
        if (epsy < 0.0f) {
            epsy = 0.0f
        } else if (epsy > drawer.epsyLimit) {
            epsy = drawer.epsyLimit
        }
        view.invalidate()
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        if (e == null) {
            return super.onSingleTapConfirmed(e)
        }
        taskClickListener.onTaskClick(drawer.findTask(e.x + epsx, e.y + epsy))
        return super.onSingleTapConfirmed(e)
    }

}