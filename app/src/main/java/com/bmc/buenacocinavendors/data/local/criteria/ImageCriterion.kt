package com.bmc.buenacocinavendors.data.local.criteria

class ImageCriterion(val image: String) : ICriterion {
    override fun toQuery(): String {
        return "image = ?"
    }

    override fun getArguments(): MutableList<String> {
        return mutableListOf(image)
    }
}