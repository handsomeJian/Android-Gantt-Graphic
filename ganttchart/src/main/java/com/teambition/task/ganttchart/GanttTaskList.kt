package com.teambition.task.ganttchart

import java.util.*

class GanttTaskList(var id: String, var name: String, var list: List<GanttTask>) {

    var size = 0

    var startTime: Date? = null
    var endTime: Date? = null
    var startSettle = false
    var endSettle = false
    var finished = false

    init {
        updateTimeScale()
    }

    private fun updateStartTime(time: Date?, state: Boolean) {
        if (startTime == null || startTime?.after(time) == true) {
            startTime = time
            startSettle = startSettle or state
        }
    }
    private fun updateEndTime(time: Date?, state: Boolean) {
        if (endTime == null || endTime?.before(time) == true) {
            endTime = time
            endSettle = endSettle or state
        }
    }

    fun updateTimeScale() {
        size = list.size
        for (i in list) {
            if (i.startTime != null) {
                updateStartTime(i.startTime, true)
                updateEndTime(i.startTime, false)
            }
            if (i.endTime != null) {
                updateStartTime(i.endTime, false)
                updateEndTime(i.endTime, true)
            }
        }
    }


}