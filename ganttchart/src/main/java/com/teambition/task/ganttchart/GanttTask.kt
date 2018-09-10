package com.teambition.task.ganttchart

import android.graphics.Canvas
import java.util.*

data class GanttTask(var id: String, var name: String, var finished: Boolean,
                var startTime: Date?, var endTime: Date?, var executor: String?)