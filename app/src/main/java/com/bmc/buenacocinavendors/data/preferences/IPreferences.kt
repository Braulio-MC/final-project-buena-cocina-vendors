package com.bmc.buenacocinavendors.data.preferences

import com.bmc.buenacocinavendors.data.network.model.GetStreamUserCredentials

interface IPreferences {
    fun set(key: String, value: String)
    fun saveUserCredentials(credentials: GetStreamUserCredentials)
    fun get(key: String): String?
    fun getUserCredentials(): GetStreamUserCredentials?
    fun remove(key: String)
    fun clearUserCredentials()
    fun clear()
}