package com.demo.utils

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class QueryBuilderTest {
    @Test
    fun testQueryBuilder() {
        val params = mutableMapOf<String, Any>()

        params["active"] = true
        params["position"] = "goalkeeper"
        params["club_id"] = 5
        params["birth_date"] = LocalDate.now() .. LocalDate.now()

        val result = QueryBuilder.build(params)

        assert(result.second != params)
        assert(result.second.count() == params.count()+1)
        assert(result.first.isNotBlank())

        Assertions.assertEquals("active = :active and lower(position) = lower(:position) and club_id = :club_id and birth_date between :birth_dateStart and :birth_dateEnd", result.first,)
    }
}