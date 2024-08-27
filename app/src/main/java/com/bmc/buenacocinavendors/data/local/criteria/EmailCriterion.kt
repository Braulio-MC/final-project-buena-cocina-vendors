package com.bmc.buenacocinavendors.data.local.criteria

class EmailCriterion(val email: String) : ICriterion {
    override fun toQuery(): String {
        return "email = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(email)
    }
}