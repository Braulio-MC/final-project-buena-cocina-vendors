package com.bmc.buenacocinavendors.data.local.criteria

class TotalCriterion(val total: Double) : ICriterion {
    override fun toQuery(): String {
        return "total = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(total.toString())
    }
}