package com.bmc.buenacocinavendors.data.local.criteria

class PhoneNumberCriterion(val phoneNumber: String) : ICriterion {
    override fun toQuery(): String {
        return "phoneNumber = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(phoneNumber)
    }
}