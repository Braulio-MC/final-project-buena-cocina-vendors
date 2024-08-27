package com.bmc.buenacocinavendors.data.local.criteria

class UserIDCriterion(val userId: String) : ICriterion {
    override fun toQuery(): String {
        return "userId = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(userId)
    }
}