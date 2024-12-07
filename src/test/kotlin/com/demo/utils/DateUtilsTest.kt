package com.demo.utils

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

class DateUtilsTest {
    @Test
    fun testStartOfYear() {
        DateUtils.startOfTheYear(2021).let {
            assert(it.year == 2021)
            assert(it.monthValue == 1)
            assert(it.dayOfMonth == 1)
        }
    }

    @Test
    fun testEndOfYear() {
        DateUtils.endOfTheYear(2021).let {
            assert(it.year == 2021)
            assert(it.monthValue == 12)
            assert(it.dayOfMonth == 31)
        }
    }
}