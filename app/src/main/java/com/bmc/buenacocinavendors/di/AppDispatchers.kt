package com.bmc.buenacocinavendors.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppDispatcher(val appDispatchers: AppDispatchers)

enum class AppDispatchers {
    IO,
}