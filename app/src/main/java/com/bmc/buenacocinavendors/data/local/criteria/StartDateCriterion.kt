package com.bmc.buenacocinavendors.data.local.criteria

import java.time.LocalDateTime

class StartDateCriterion(val at: LocalDateTime) : ICriterion {
    override fun toQuery(): String {
        return "startDate = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(at.toString())
    }
}