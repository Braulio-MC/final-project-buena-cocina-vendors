package com.bmc.buenacocinavendors.data.local.criteria

class StoreNameCriterion(val storeName: String) : ICriterion {
    override fun toQuery(): String {
        return "storeName = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(storeName)
    }
}