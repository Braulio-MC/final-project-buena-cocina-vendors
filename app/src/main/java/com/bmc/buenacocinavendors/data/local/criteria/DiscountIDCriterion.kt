package com.bmc.buenacocinavendors.data.local.criteria

class DiscountIDCriterion(val discountId: String) : ICriterion {
    override fun toQuery(): String {
        return "discountId = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(discountId)
    }
}