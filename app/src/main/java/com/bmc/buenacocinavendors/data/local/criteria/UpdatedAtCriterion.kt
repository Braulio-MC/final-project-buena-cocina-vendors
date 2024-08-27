package com.bmc.buenacocinavendors.data.local.criteria

import java.time.LocalDateTime

class UpdatedAtCriterion(val at : LocalDateTime) : ICriterion {
    override fun toQuery(): String {
        return "updatedAt = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(at.toString())
    }
}