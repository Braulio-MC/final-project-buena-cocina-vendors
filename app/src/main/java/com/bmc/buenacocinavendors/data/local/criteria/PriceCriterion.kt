package com.bmc.buenacocinavendors.data.local.criteria

class PriceCriterion(val price: Double) : ICriterion {
    override fun toQuery(): String {
        return "price = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(price.toString())
    }
}