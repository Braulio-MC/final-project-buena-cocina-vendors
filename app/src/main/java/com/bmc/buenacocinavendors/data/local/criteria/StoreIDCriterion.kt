package com.bmc.buenacocinavendors.data.local.criteria

class StoreIDCriterion(val storeId: String) : ICriterion {
    override fun toQuery(): String {
        return "storeId = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(storeId)
    }
}