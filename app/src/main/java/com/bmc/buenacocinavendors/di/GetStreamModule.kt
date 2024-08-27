package com.bmc.buenacocinavendors.di

import android.content.Context
import com.bmc.buenacocinavendors.core.GET_STREAM_API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetStreamModule {
    @Provides
    fun provideOfflinePluginFactory(@ApplicationContext context: Context): StreamOfflinePluginFactory {
        return StreamOfflinePluginFactory(context)
    }

    @Provides
    @Singleton
    fun provideChatClient(
        @ApplicationContext context: Context,
        offlinePluginFactory: StreamOfflinePluginFactory
    ): ChatClient {
        val client = ChatClient.Builder(GET_STREAM_API_KEY, context)
            .withPlugins(offlinePluginFactory)
            .logLevel(ChatLogLevel.ALL) // Set to nothing on production
            .build()
        return client
    }
}