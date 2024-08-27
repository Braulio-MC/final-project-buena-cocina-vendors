package com.bmc.buenacocinavendors.data.local.criteria

class DiscountPercentageCriterion(val percentage: Double) : ICriterion {
    override fun toQuery(): String {
        return "discountPercentage = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(percentage.toString())
    }
}