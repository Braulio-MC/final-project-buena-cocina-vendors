package com.bmc.buenacocinavendors.data.local.criteria

import java.time.LocalDateTime

class EndDateCriterion(val at: LocalDateTime) : ICriterion {
    override fun toQuery(): String {
        return "endDate = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(at.toString())
    }
}