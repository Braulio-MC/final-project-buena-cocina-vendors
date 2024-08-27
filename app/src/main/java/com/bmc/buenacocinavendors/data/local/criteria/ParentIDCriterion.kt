package com.bmc.buenacocinavendors.data.local.criteria

class ParentIDCriterion(val parentId: String) : ICriterion {
    override fun toQuery(): String {
        return "parentId = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(parentId)
    }
}