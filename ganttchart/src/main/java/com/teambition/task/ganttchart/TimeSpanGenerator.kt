package com.teambition.task.ganttchart

import java.util.*

enum class TimeScale {
    DAY,
    WEEK,
    MONTH,
    SEASON,
    YEAR
}

interface TimeSpanGenerator {
    fun getUnitDp(): Float
    fun getLeftTimePadding(time: Date): Date
    fun getRightTimePadding(time: Date): Date
    fun getTimePlace(time: Date, startTime: Date): Float
    fun getTimeString(time: Date): String
    fun addUnitTime(time: Date): Date
}

class DayTimeSpanGenerator: TimeSpanGenerator {

    private fun getTime(time: Date): Calendar = Calendar.getInstance().apply {
        this.time = time
        set(Calendar.HOUR_OF_DAY, this.getActualMinimum(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, this.getActualMinimum(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    override fun getUnitDp() = 80.0f

    override fun getLeftTimePadding(time: Date): Date = getTime(time).apply{ add(Calendar.DATE, -7) }.time

    override fun getRightTimePadding(time: Date): Date = getTime(time).apply{ add(Calendar.DATE, 7) }.time

    override fun getTimePlace(time: Date, startTime: Date): Float {
        val tmpTime = getTime(time)
        var tmp = ((tmpTime.timeInMillis - getTime(startTime).timeInMillis) / (1000 * 3600 * 24).toFloat())
        val tmpHour = tmpTime.get(Calendar.HOUR)
        if ((tmpHour > 9) and (tmpHour < 18)) {
            tmp += (tmpHour - 9) / 9.0f
        } else if (tmpHour >= 18) {
            tmp += 1
        }
        return tmp
    }

    override fun getTimeString(time: Date): String {
        val tmp = getTime(time)
        return "${tmp.get(Calendar.MONTH) + 1} 月 ${tmp.get(Calendar.DATE)} 日"
    }

    override fun addUnitTime(time: Date): Date = getTime(time).apply { add(Calendar.DATE, 1) }.time
}


class WeekTimeSpanGenerator: TimeSpanGenerator {

    private fun getTime(time: Date): Calendar = Calendar.getInstance().apply {
        this.time = time
        set(Calendar.HOUR_OF_DAY, this.getActualMinimum(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, this.getActualMinimum(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.DAY_OF_WEEK, this.firstDayOfWeek)
    }

    override fun getUnitDp() = 135.0f

    override fun getLeftTimePadding(time: Date): Date = getTime(time).apply{ add(Calendar.DATE, -7) }.time

    override fun getRightTimePadding(time: Date): Date = getTime(time).apply{ add(Calendar.DATE, 14) }.time

    override fun getTimePlace(time: Date, startTime: Date): Float {
        return  ((time.time - getTime(startTime).timeInMillis) / (1000 * 3600 * 24 * 7).toFloat())
    }

    override fun getTimeString(time: Date): String {
        val tmp = getTime(time)
        var ans = "${tmp.get(Calendar.MONTH) + 1}.${tmp.get(Calendar.DATE)} - "
        tmp.add(Calendar.DATE, 6)
        ans += "${tmp.get(Calendar.MONTH) + 1}.${tmp.get(Calendar.DATE)} "
        ans += "第 ${tmp.get(Calendar.WEEK_OF_YEAR)} 周"
        return ans
    }

    override fun addUnitTime(time: Date): Date = getTime(time).apply { add(Calendar.DATE, 7) }.time
}

class MonthTimeSpanGenerator: TimeSpanGenerator {

    private fun getTime(time: Date): Calendar = Calendar.getInstance().apply {
        this.time = time
        set(Calendar.HOUR_OF_DAY, this.getActualMinimum(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, this.getActualMinimum(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    override fun getUnitDp() = 200.0f

    override fun getLeftTimePadding(time: Date): Date = getTime(time).apply{ add(Calendar.MONTH, -1) }.time

    override fun getRightTimePadding(time: Date): Date = getTime(time).apply{ add(Calendar.MONTH, 2) }.time

    override fun getTimePlace(time: Date, startTime: Date): Float {
        var ans = 0.0f
        val tmpTime = getTime(time)
        val tmpStartTime = getTime(startTime)
        while (!((tmpTime.get(Calendar.YEAR) == tmpStartTime.get(Calendar.YEAR)) and
                (tmpTime.get(Calendar.MONTH) == tmpStartTime.get(Calendar.MONTH)))) {
            ans += 1.0f
            tmpStartTime.add(Calendar.MONTH, 1)
        }
        ans += (time.time - tmpTime.timeInMillis) / (1000.toFloat() * 3600 * 24 * 7 * 30)
        return ans
    }

    override fun getTimeString(time: Date): String {
        val tmp = getTime(time)
        return "${tmp.get(Calendar.YEAR)}.${tmp.get(Calendar.MONTH)}"
    }

    override fun addUnitTime(time: Date): Date = getTime(time).apply { add(Calendar.MONTH, 1) }.time
}

