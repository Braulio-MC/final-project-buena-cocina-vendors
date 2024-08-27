package com.bmc.buenacocinavendors.data.local.criteria

class QuantityCriterion(val quantity: Int) : ICriterion {
    override fun toQuery(): String {
        return "quantity = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(quantity.toString())
    }
}