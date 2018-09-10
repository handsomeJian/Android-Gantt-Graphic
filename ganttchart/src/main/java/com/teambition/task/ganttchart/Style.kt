package com.teambition.task.ganttchart

import android.graphics.Paint

class Style(private val scale: Float) {

    val lineHeight = 28.0f  // dp
    val taskHeight = 22.0f  // dp
    val groupHeight = 17.0f
    val textSize = 22.0f    // dp
    val taskLineBlank = (lineHeight - taskHeight) / 2.0f
    val groupLineBlank = (lineHeight - groupHeight) / 2.0f
    val textLineBlank = 8.0f
    val unsettleTaskLength = 36.0f
    val textTaskBlank = 15.0f
    val minTaskWidth = 5.0f

    val taskPaint= Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        setARGB(255, 61,168,245)
    }
    val textPaint: Paint
    val backgroundTextPaint: Paint
    val finishedTaskPaint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        setARGB(255, 210,210,210)
    }
    val passedTaskPaint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        setARGB(255, 252, 97, 86)
    }
    val backLinePaint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 2.0f
        setARGB(255, 218,218,218)
    }
    val backBlockPaint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        setARGB(25, 61,168,245)
    }

    init {
        textPaint = Paint().apply {
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            textSize = dpToPx(textSize)
            setARGB(255, 56, 56, 56)
        }
        backgroundTextPaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            textSize = dpToPx(textSize)
            setARGB(255, 56, 56, 56)
        }
    }

    fun dpToPx(dpValue: Float) = dpValue * scale
    fun pxToDp(pxValue: Float) = pxValue / scale

}