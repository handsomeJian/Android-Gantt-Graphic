package com.teambition.task.ganttchart

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

object Painter {

    private fun drawTriangle(canvas: Canvas,
                     p0x: Float, p0y: Float,
                     p1x: Float, p1y: Float,
                     p2x: Float, p2y: Float,
                     paint: Paint) {
        val path = Path().apply {
            moveTo(p0x, p0y)
            lineTo(p1x, p1y)
            lineTo(p2x, p2y)
            close()
        }
        canvas.drawPath(path, paint)
    }

    private fun drawRect(canvas: Canvas,
                         left: Float, top: Float, right: Float, bottom: Float,
                         paint: Paint) {
        canvas.drawRect(left, top, right, bottom, paint)
    }

    fun drawGroup(canvas: Canvas,
                  startPos: Float, endPos: Float,
                  topPos: Float, bottomPos: Float,
                  paint: Paint) {
        val divide = topPos + (bottomPos - topPos) * 0.7f
        drawRect(canvas,
            startPos, topPos, endPos, divide,
            paint)
        drawTriangle(canvas,
            startPos, divide,
            startPos + bottomPos - divide, divide,
            startPos, bottomPos,
            paint)
        drawTriangle(canvas,
            endPos, divide,
            endPos - bottomPos + divide, divide,
            endPos, bottomPos,
            paint)
    }

    fun drawLine(canvas: Canvas, startx: Float, starty: Float, endx: Float, endy: Float, paint: Paint) {
        canvas.drawLine(startx, starty, endx, endy, paint)
    }

    /*fun drawTask(canvas: Canvas,
                 left: Float, top: Float,
                 right: Float, bottom: Float,
                 paint: Paint) {
        canvas.drawRoundRect(left, top,
                             right, bottom,
                         10.0f, 10.0f,
                             paint)
        drawRect(canvas, left, top, right, bottom, paint)
    }*/

    fun drawTask(canvas: Canvas,
                          left: Float, top: Float,
                          right: Float, bottom: Float,
                          paint: Paint, type: Int) {
        when (type) {
            0 -> drawRect(canvas, left, top, right, bottom, paint)  // normal task
            1 -> {                                                  // left unsettled
                val divide = (bottom - top) * 0.5f / 1.7f
                drawRect(canvas, left + divide - 2, top, right, bottom, paint)
                drawTriangle(canvas,
                    left, top + (bottom - top) * 0.5f,
                    left + divide, top,
                    left + divide, bottom,
                    paint)
            }
            2 -> {                                                  // right unsettled
                val divide = (bottom - top) * 0.5f / 1.7f
                drawRect(canvas, left, top, right - divide + 2, bottom, paint)
                drawTriangle(canvas,
                    right, top + (bottom - top) * 0.5f,
                    right - divide, top,
                    right - divide, bottom,
                    paint)
            }
        }
    }

    fun drawText(canvas: Canvas, text: String, startx: Float, starty: Float, paint: Paint) {
        canvas.drawText(text, startx, starty, paint)
    }

    fun drawTodayBackground(canvas: Canvas, startx: Float, endx: Float, height: Float, paint: Paint) {
        canvas.drawRect(startx, 0.0f, endx, height, paint)
    }

}