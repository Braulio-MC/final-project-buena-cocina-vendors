package com.bmc.buenacocinavendors.data.local.criteria

class StatusCriterion(val status: String) : ICriterion {
    override fun toQuery(): String {
        return "status = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(status)
    }
}