package com.bmc.buenacocinavendors.data.local.criteria

class CategoryParentCriterion(val categoryParent: String) : ICriterion {
    override fun toQuery(): String {
        return "categoryParent = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(categoryParent)
    }
}