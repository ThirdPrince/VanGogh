package com.vangogh.media.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @ClassName MediaTimeUtils
 * @Description  media time
 * @Author dhl
 * @Date 2021/2/8 9:36
 * @Version 1.0
 */
object MediaTimeUtils {



    /**
     * 获取media 格式化时间
     *
     * @param timestamp
     * @return
     */
    fun getMediaTime(timestamp: Long): String {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date()
        val mediaCalendar = Calendar.getInstance()
        mediaCalendar.timeInMillis = timestamp
        return if (currentCalendar[Calendar.DAY_OF_YEAR] == mediaCalendar[Calendar.DAY_OF_YEAR] && currentCalendar[Calendar.YEAR] == mediaCalendar[Calendar.YEAR]) {
            "今天"
        } else if (currentCalendar[Calendar.WEEK_OF_YEAR] == mediaCalendar[Calendar.WEEK_OF_YEAR] && currentCalendar[Calendar.YEAR] == mediaCalendar[Calendar.YEAR]) {
            "本周"
        } else if (currentCalendar[Calendar.MONTH] == mediaCalendar[Calendar.MONTH] && currentCalendar[Calendar.YEAR] == mediaCalendar[Calendar.YEAR]) {
            "本月"
        } else {
            val date = Date(timestamp)
            val sdf = SimpleDateFormat("yyyy/MM")
            sdf.format(date)
        }
    }
}