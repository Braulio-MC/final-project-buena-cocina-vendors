package com.bmc.buenacocinavendors.data.local.criteria

class DeliveryLocationNameCriterion(val deliveryLocationName: String) : ICriterion {
    override fun toQuery(): String {
        return "deliveryLocationName = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(deliveryLocationName)
    }
}