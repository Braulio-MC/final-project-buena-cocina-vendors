package com.bmc.buenacocinavendors.data.local.criteria

class CategoryNameCriterion(val categoryName: String) : ICriterion {
    override fun toQuery(): String {
        return "categoryName = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(categoryName)
    }
}