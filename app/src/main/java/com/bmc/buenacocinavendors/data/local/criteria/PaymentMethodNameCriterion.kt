package com.bmc.buenacocinavendors.data.local.criteria

class PaymentMethodNameCriterion(val paymentMethodName: String) : ICriterion {
    override fun toQuery(): String {
        return "paymentMethodName = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(paymentMethodName)
    }
}