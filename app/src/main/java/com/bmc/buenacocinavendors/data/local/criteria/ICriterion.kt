package com.bmc.buenacocinavendors.data.local.criteria

interface ICriterion {
    fun toQuery(): String
    fun getArguments(): MutableList<String>
}