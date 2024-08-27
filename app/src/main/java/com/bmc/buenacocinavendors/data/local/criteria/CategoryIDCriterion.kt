package com.bmc.buenacocinavendors.data.local.criteria

class CategoryIDCriterion(val categoryId: String) : ICriterion {
    override fun toQuery(): String {
        return "categoryId = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(categoryId)
    }
}