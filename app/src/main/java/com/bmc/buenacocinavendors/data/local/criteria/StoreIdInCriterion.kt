package com.bmc.buenacocinavendors.data.local.criteria

class StoreIdInCriterion(val inValues: List<String>) : ICriterion {
    override fun toQuery(): String {
        return when {
            inValues.contains("") && inValues.size == 1 -> {
                "storeId = ''"
            }

            inValues.contains("") -> {
                "storeId = '' OR storeId IN (?)"
            }

            inValues.isNotEmpty() -> {
                "storeId IN (?)"
            }

            else -> {
                "1 = 0"
            }
        }
    }

    override fun getArguments(): MutableList<String> {
        val values = inValues.toMutableList()
        values.remove("")
        return values
    }
}