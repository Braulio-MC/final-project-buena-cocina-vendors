package com.bmc.buenacocinavendors.data.local.criteria

class RatingCriterion(val rating: Double) : ICriterion {
    override fun toQuery(): String {
        return "rating = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(rating.toString())
    }
}