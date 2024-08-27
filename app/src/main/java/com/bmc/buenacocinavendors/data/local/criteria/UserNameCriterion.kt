package com.bmc.buenacocinavendors.data.local.criteria

class UserNameCriterion(val userName: String) : ICriterion {
    override fun toQuery(): String {
        return "userName = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(userName)
    }
}