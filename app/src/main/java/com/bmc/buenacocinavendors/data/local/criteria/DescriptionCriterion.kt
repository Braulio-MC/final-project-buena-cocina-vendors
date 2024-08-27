package com.bmc.buenacocinavendors.data.local.criteria

class DescriptionCriterion(val description: String) : ICriterion {
    override fun toQuery(): String {
        return "description = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(description)
    }
}