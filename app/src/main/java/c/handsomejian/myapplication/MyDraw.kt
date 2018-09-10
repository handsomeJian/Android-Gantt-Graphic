package c.handsomejian.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

class MyDraw(private val scale: Float) {

    var paint: Paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 1f
        color = Color.GRAY
    }
    var height: Float = 0.0f
    var width: Float = 0.0f
    var columnHeight: Float = 128.0f
    var columnWidth: Float = 0.0f

    fun dpToPx(dpValue: Float) = dpValue * scale
    fun pxToDp(pxValue: Float) = pxValue / scale

    private fun getSize(canvas: Canvas, number: Int) {
        height = canvas.height.toFloat()
        width = canvas.width.toFloat()
        columnWidth = width / number
        columnHeight = dpToPx(24.0f)
    }

    fun drawBackgroundLine(canvas: Canvas, number: Int) {
        paint.setARGB(255, 228,228,228)
        getSize(canvas, number)
        var nowPlace = columnWidth
        while (nowPlace < width) {
            canvas.drawLine(nowPlace,0.0f, nowPlace, height, paint)
            nowPlace += columnWidth
        }
    }

    fun drawText(canvas: Canvas, text: String, line: Int, start: Float) {
        paint.setARGB(255, 128,128,128)
        paint.textSize = dpToPx(16.0f)
        canvas.drawText(text, (start + 0.1f) * columnWidth, (line - 0.2f) * columnHeight, paint)
    }

    fun drawTriangle(canvas: Canvas,
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

    fun drawRange(canvas: Canvas, line: Int, startTime: Float, endTime: Float) {
        var downLength = 30.0f
        paint.setARGB(255, 61,168,245)
        canvas.drawRect(startTime * (columnWidth - 1), (line - 0.7f) * columnHeight,
                            endTime * columnWidth, (line - 0.3f) * columnHeight, paint)
        drawTriangle(canvas, startTime * (columnWidth - 1), (line - 0.7f) * columnHeight,
            startTime * (columnWidth - 1), (line - 0.3f) * columnHeight + downLength,
            startTime * (columnWidth - 1) + downLength, (line - 0.3f) * columnHeight, paint)
        drawTriangle(canvas, endTime * columnWidth, (line - 0.7f) * columnHeight,
            endTime * columnWidth, (line - 0.3f) * columnHeight + downLength,
            endTime * columnWidth - downLength, (line - 0.3f) * columnHeight, paint)
    }

    fun drawTask(canvas: Canvas, line: Int, startTime: Float, endTime: Float) {
        paint.setARGB(255, 61,168,245)
        //canvas.drawRect(startTime * (columnWidth - 1), (line - 1) * columnHeight, endTime * columnWidth, line * columnHeight, paint)
        //canvas.drawOval(startTime * (columnWidth - 1), (line - 1) * columnHeight, endTime * columnWidth, line * columnHeight, paint)
        canvas.drawRoundRect(startTime * (columnWidth - 1), (line - 1) * columnHeight,
                                endTime * columnWidth, line * columnHeight,
                                20.0f, 20.0f,
                                paint)
    }
}