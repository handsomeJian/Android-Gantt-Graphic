package c.handsomejian.myapplication

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.teambition.task.ganttchart.*
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
        //var tmpTask2 = GanttTask("2", "kkk", false, startTime.time, null, null)
        var tmpTask2 = GanttTask("2", "kkk", false, null, startTime.time, null)
        //var tmpTask3 = GanttTask("3", "ooo", false, startTime.time, endTime.time, "handsomeJian")
        var tmpTask3 = GanttTask("3", "ooo", false, null, endTime.time, "handsomeJian")

        var tmpTaskList = GanttTaskList("4", "g_list", listOf(tmpTask1, tmpTask2, tmpTask3))
        var tmpTaskList2 = GanttTaskList("5", "h_list", listOf(tmpTask1, tmpTask2, tmpTask3))
        var tmpTaskList3 = GanttTaskList("6", "k_list", listOf(tmpTask1, tmpTask2, tmpTask3))
        var tmpTaskList4 = GanttTaskList("7", "o_list", listOf(tmpTask1, tmpTask2, tmpTask3))
        var tmpTaskList5 = GanttTaskList("7", "o_list", listOf(tmpTask1, tmpTask2, tmpTask3))
        var tmpTaskList6 = GanttTaskList("7", "o_list", listOf(tmpTask1, tmpTask2, tmpTask3))
        var tmpTaskList7 = GanttTaskList("7", "o_list", listOf(tmpTask1, tmpTask2, tmpTask3))


        if (context != null) {
            myDraw = RenderEngine(context, this, listOf(tmpTaskList, tmpTaskList2, tmpTaskList3, tmpTaskList4, tmpTaskList5, tmpTaskList6, tmpTaskList7),
                WeekTimeSpanGenerator(),
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