package c.handsomejian.myapplication

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.teambition.task.ganttchart.GanttTask
import com.teambition.task.ganttchart.GanttTaskList
import com.teambition.task.ganttchart.MonthTimeSpanGenerator
import com.teambition.task.ganttchart.RenderEngine
import java.util.*

class MyView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var myDraw: RenderEngine

    init {
        var startTime = Calendar.getInstance()
        var endTime = Calendar.getInstance()
        endTime.add(Calendar.DATE, -1)
        startTime.add(Calendar.DATE, -2)
        Log.v("HERE", "${startTime.time}    ${endTime.time}")
        var tmpTask1 = GanttTask("1", "233", false, null, endTime.time, null)
        endTime.add(Calendar.DATE, -3)
        startTime.add(Calendar.DATE, -4)
        Log.v("HERE", "${startTime.time}    ${endTime.time}")
        var tmpTask2 = GanttTask("2", "kkk", false, startTime.time, null, null)

        var tmpTaskList = GanttTaskList("3", "g_list", listOf(tmpTask1, tmpTask2))


        if (context != null) {
            myDraw = RenderEngine(context, this, listOf(tmpTaskList), MonthTimeSpanGenerator(),
                object : RenderEngine.TaskClickListener {
                    override fun onTaskClick(id: String?) {
                        Log.v("HERE", "$id")
                    }
                })
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        //canvas.translate(myDraw.style.dpToPx(-myDraw.timeSpanGenerator.getUnitDp() * 7.0f), 0.0f)
        myDraw.render(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            myDraw.onTouchEvent(event)
        }
        return true
    }

    /*override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            //myDraw.eps +=
            invalidate()
        }
    }*/

}