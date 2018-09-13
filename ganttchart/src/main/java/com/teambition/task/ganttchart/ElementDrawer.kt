package com.teambition.task.ganttchart

import android.graphics.Canvas
import android.util.Log
import java.util.*

class ElementDrawer(var list: List<GanttTaskList>, val style: Style, val timeSpanGenerator: TimeSpanGenerator) {

    var size = 0

    var startTime: Date? = null
    var endTime: Date? = null
    lateinit var nowTime: Date

    var length = 0.0f
    var height = 0.0f
    var epsxLimit = 0.0f
    var epsyLimit = 0.0f

    var canvasWidth = 0.0f
    var canvasHeight = 0.0f

    init {
        updateTimeScale()
        updateSize()
    }

    private fun updateSize() {
        size = 0
        for (i in list) {
            size += i.size + 1
        }
        height = (size + 1) * style.lineHeight
    }

    private fun updateTimeScale() {
        startTime = null
        endTime = null
        for (i in list) {
            if (i.startTime != null) {
                if (startTime == null || startTime?.after(i.startTime) == true) {
                    startTime = i.startTime
                }
            }
            if (i.endTime != null) {
                if (endTime == null || endTime?.before(i.endTime) == true) {
                    endTime = i.endTime
                }
            }
        }

        //Log.v("HERE", "Before $startTime,  $endTime")
        if (startTime != null && endTime != null) {
            startTime = timeSpanGenerator.getLeftTimePadding(startTime!!)
            endTime  = timeSpanGenerator.getRightTimePadding(endTime!!)
        }
        //Log.v("HERE", "After $startTime,  $endTime")

        length = timeSpanGenerator.getTimePlace(endTime!!, startTime!!) * timeSpanGenerator.getUnitDp()

        epsxLimit = style.dpToPx(length) - canvasWidth
        epsyLimit = style.dpToPx(height) - canvasHeight
        if (epsyLimit < 0.0f) {
            epsyLimit = 0.0f
        }

        nowTime = Date(System.currentTimeMillis())
    }

    private fun drawBackground(canvas: Canvas) {

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, this.getActualMinimum(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, this.getActualMinimum(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
        }
        if ((today.time >= startTime) and (today.time <= endTime)) {
            if (timeSpanGenerator.getTimePlace(today.time, startTime!!) == 12.0f) {
                Log.v("Here", "haha")
            }
            val tmp1 = timeSpanGenerator.getTimePlace(today.time, startTime!!) * timeSpanGenerator.getUnitDp()
            if (timeSpanGenerator.getTimePlace(today.time, startTime!!) == 12.0f) {
                Log.v("Here", "haha")
            }
            today.add(Calendar.DATE, 1)
            val tmp2 = timeSpanGenerator.getTimePlace(today.time, startTime!!) * timeSpanGenerator.getUnitDp()
            Painter.drawTodayBackground(canvas, style.dpToPx(tmp1), style.dpToPx(tmp2), style.dpToPx(height), style.backBlockPaint)
        }

        var tmpTime = Date(startTime!!.time)
        Painter.drawLine(canvas, 0.0f, style.dpToPx(style.lineHeight),
            style.dpToPx(length), style.dpToPx(style.lineHeight), style.backLinePaint)
        while (tmpTime <= endTime) {
            val tmp = timeSpanGenerator.getTimePlace(tmpTime, startTime!!) * timeSpanGenerator.getUnitDp()
            Painter.drawLine(canvas, style.dpToPx(tmp), 0.0f, style.dpToPx(tmp), style.dpToPx(height), style.backLinePaint)
            Painter.drawText(canvas, timeSpanGenerator.getTimeString(tmpTime),
                style.dpToPx(tmp + timeSpanGenerator.getUnitDp() / 2.0f),
                style.dpToPx(style.lineHeight - style.textLineBlank), style.backgroundTextPaint)
            tmpTime = timeSpanGenerator.addUnitTime(tmpTime)
        }
    }

    private fun drawTask(canvas: Canvas, task: GanttTask, nowLine: Int): Int {
        if ((task.startTime == null) and (task.endTime == null)) {
            return 0
        }
        var startPlace: Float
        var endPlace: Float
        when {
            task.startTime == null -> {
                endPlace = timeSpanGenerator.getTimePlace(task.endTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
                startPlace = endPlace - style.unsettleTaskLength

            }
            task.endTime == null -> {
                startPlace = timeSpanGenerator.getTimePlace(task.startTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
                endPlace = startPlace + style.unsettleTaskLength
            }
            else -> {
                startPlace = timeSpanGenerator.getTimePlace(task.startTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
                endPlace = timeSpanGenerator.getTimePlace(task.endTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
            }
        }

        if (endPlace - startPlace < style.minTaskWidth) {
            endPlace = startPlace + style.minTaskWidth
        }

        Painter.drawTask(canvas, style.dpToPx(startPlace), style.dpToPx(nowLine * style.lineHeight + style.taskLineBlank),
            style.dpToPx(endPlace), style.dpToPx((nowLine + 1) * style.lineHeight - style.taskLineBlank),
            when {
                task.finished -> style.finishedTaskPaint
                (task.endTime != null) -> {
                    if (task.endTime!!.before(Calendar.getInstance().time)) {
                        style.passedTaskPaint
                    } else {
                        style.taskPaint
                    }
                }
                else -> style.taskPaint
            },
            when {
                task.startTime == null -> 1
                task.endTime == null -> 2
                else -> 0
            })

        Painter.drawText(canvas, task.name + if (task.executor != null) {
            " ~ ${task.executor}"
            } else {
            " "
            },
            style.dpToPx(endPlace + style.textTaskBlank),
            style.dpToPx((nowLine + 1) * style.lineHeight - style.textLineBlank),
            style.textPaint)
        return 1
    }

    private fun drawTaskList(canvas: Canvas, taskList: GanttTaskList, preSize: Int): Int {

        if ((taskList.startTime == null) or (taskList.endTime == null)) {
            return 0
        }

        var startPlace = timeSpanGenerator.getTimePlace(taskList.startTime!!, startTime!!)
        var endPlace = timeSpanGenerator.getTimePlace(taskList.endTime!!, startTime!!)
        startPlace *= timeSpanGenerator.getUnitDp()
        endPlace *= timeSpanGenerator.getUnitDp()
        if (!taskList.startSettle) {
            startPlace -= style.unsettleTaskLength
        }
        if (!taskList.endSettle) {
            endPlace += style.unsettleTaskLength
        }
        if (endPlace - startPlace < style.minTaskWidth) {
            endPlace = startPlace + style.minTaskWidth
        }

        Painter.drawGroup(canvas, style.dpToPx(startPlace), style.dpToPx(endPlace),
            style.dpToPx(preSize * style.lineHeight + style.groupLineBlank),
            style.dpToPx((preSize + 1) * style.lineHeight - style.groupLineBlank),
            if (taskList.finished) {
                style.finishedTaskPaint
            } else {
                style.taskPaint
            })
        Painter.drawText(canvas, taskList.name,
            style.dpToPx(endPlace + style.textTaskBlank),
            style.dpToPx((preSize + 1) * style.lineHeight - style.textLineBlank),
            style.textPaint)

        var nowSize = preSize + 1
        for (i in taskList.list) {
            nowSize += drawTask(canvas, i, nowSize)
        }
        return nowSize - preSize
    }

    fun draw(canvas: Canvas, epsx: Float, epsy: Float) {

        canvasWidth = canvas.width.toFloat()
        canvasHeight = canvas.height.toFloat()

        updateSize()
        updateTimeScale()

        if (endTime == null || startTime == null) {
            return
        }

        if (height < style.pxToDp(canvasHeight)) {
            height = style.pxToDp(canvasHeight)
        }

        canvas.translate(-epsx, -epsy)

        drawBackground(canvas)

        var nowSize = 1
        for (i in list) {
            nowSize += drawTaskList(canvas, i, nowSize)
        }
    }

    fun getInitPlace(canvas: Canvas): Float {
        canvasWidth = canvas.width.toFloat()
        canvasHeight = canvas.height.toFloat()
        updateSize()
        updateTimeScale()
        if (endTime == null || startTime == null) {
            return 0.0f
        }
        if (height < style.pxToDp(canvasHeight)) {
            height = style.pxToDp(canvasHeight)
        }
        val today = Calendar.getInstance()
        when {
            today.time < startTime -> return 0.0f
            today.time > endTime -> return epsxLimit
            else -> {
                val tmp = style.dpToPx((timeSpanGenerator.getTimePlace(today.time, startTime!!) - 2) * timeSpanGenerator.getUnitDp())
                when {
                    tmp < 0.0f -> return 0.0f
                    tmp > epsxLimit -> return epsxLimit
                    else -> return tmp
                }
            }
        }
    }

    fun findTask(x: Float, y: Float): String? {
        var nowSize = 1
        for (i in list) {
            if ((i.startTime == null) and (i.endTime == null)) {
                continue
            }
            nowSize += 1
            for (task in i.list) {
                if ((task.startTime == null) and (task.endTime == null)) {
                    continue
                }
                val startPlace: Float
                val endPlace: Float
                when {
                    task.startTime == null -> {
                        endPlace = timeSpanGenerator.getTimePlace(task.endTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
                        startPlace = endPlace - style.unsettleTaskLength
                    }
                    task.endTime == null -> {
                        startPlace = timeSpanGenerator.getTimePlace(task.startTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
                        endPlace = startPlace + style.unsettleTaskLength
                    }
                    else -> {
                        startPlace = timeSpanGenerator.getTimePlace(task.startTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
                        endPlace = timeSpanGenerator.getTimePlace(task.endTime!!, startTime!!) * timeSpanGenerator.getUnitDp()
                    }
                }
                if ((x >= style.dpToPx(startPlace)) and (y >= style.dpToPx(nowSize * style.lineHeight + style.taskLineBlank))
                    and (x <= style.dpToPx(endPlace)) and (y <= style.dpToPx((nowSize + 1) * style.lineHeight - style.taskLineBlank))) {
                    return task.id
                }
                nowSize += 1
            }
        }
        return null
    }

}