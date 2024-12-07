package com.demo.utils

import java.util.stream.Collectors

object QueryBuilder {
    private fun getQuery(entry: Map.Entry<String, Any>) : String {
        return when (entry.value) {
            is String -> "lower(${entry.key}) = lower(:${entry.key})"
            is ClosedRange<*> -> "${entry.key} between :${entry.key}Start and :${entry.key}End"
            else -> "${entry.key} = :${entry.key}"
        }
    }

    fun <T: Map<String,Any>> build(params: T): Pair<String, Map<String,Any>> {
        val query = params.entries.stream()
            .map { getQuery(it) }
            .collect(Collectors.joining(" and "))

        val p = params.entries
            .map {
                when (it.value) {
                    is ClosedRange<*> -> {
                        val range = it.value as ClosedRange<*>
                        mapOf(
                            "${it.key}Start" to range.start,
                            "${it.key}End" to range.endInclusive
                        )
                    }
                    else -> mapOf(it.key to it.value)
                }
            }
            .reduce { acc, map -> acc + map }


        return Pair(query, p)
    }
}