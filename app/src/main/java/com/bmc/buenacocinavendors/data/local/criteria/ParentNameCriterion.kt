package com.bmc.buenacocinavendors.data.local.criteria

class ParentNameCriterion(val parentName: String) : ICriterion {
    override fun toQuery(): String {
        return "parentName = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(parentName)
    }
}