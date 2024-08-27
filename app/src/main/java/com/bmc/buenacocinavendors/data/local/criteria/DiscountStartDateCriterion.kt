package com.bmc.buenacocinavendors.data.local.criteria

import java.time.LocalDateTime

class DiscountStartDateCriterion(val at: LocalDateTime) : ICriterion {
    override fun toQuery(): String {
        return "discountStartDate = ?"
    }

    override fun getArguments(): MutableList<String> {
       return mutableListOf(at.toString())
    }
}