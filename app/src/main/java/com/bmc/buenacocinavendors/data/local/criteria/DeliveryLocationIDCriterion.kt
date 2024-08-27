package com.bmc.buenacocinavendors.data.local.criteria

class DeliveryLocationIDCriterion(val deliveryLocationID: String) : ICriterion {
    override fun toQuery(): String {
        return "deliveryLocationId = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(deliveryLocationID)
    }
}