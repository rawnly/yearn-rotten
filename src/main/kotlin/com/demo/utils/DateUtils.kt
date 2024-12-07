package com.demo.utils

import java.time.LocalDate

object DateUtils {
    fun endOfTheYear(year: Int): LocalDate {
        return LocalDate.of(year, 12, 31)
    }

    fun startOfTheYear(year: Int): LocalDate {
        return LocalDate.of(year, 1, 1)
    }
}
