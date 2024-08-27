package com.bmc.buenacocinavendors.data.local.criteria

import java.time.LocalDateTime

class CreatedAtCriterion(val at: LocalDateTime) : ICriterion {
    override fun toQuery(): String {
        return "createdAt = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(at.toString())
    }
}