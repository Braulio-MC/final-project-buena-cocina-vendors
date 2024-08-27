package com.bmc.buenacocinavendors.data.local.criteria

class NameCriterion(val name: String) : ICriterion {
    override fun toQuery(): String {
        return "name = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(name)
    }
}