package com.bmc.buenacocinavendors.data.local.criteria

class PercentageCriterion(val percentage: Double) : ICriterion {
    override fun toQuery(): String {
        return "percentage = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(percentage.toString())
    }
}