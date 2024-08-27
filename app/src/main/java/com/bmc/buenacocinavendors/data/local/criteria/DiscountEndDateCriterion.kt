package com.bmc.buenacocinavendors.data.local.criteria

import java.time.LocalDateTime

class DiscountEndDateCriterion(val at: LocalDateTime) : ICriterion {
    override fun toQuery(): String {
        return "discountEndDate = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(at.toString())
    }
}