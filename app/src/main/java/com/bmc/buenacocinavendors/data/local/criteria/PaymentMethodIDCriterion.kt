package com.bmc.buenacocinavendors.data.local.criteria

class PaymentMethodIDCriterion(val paymentMethodID: String) : ICriterion {
    override fun toQuery(): String {
        return "paymentMethodId = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(paymentMethodID)
    }
}