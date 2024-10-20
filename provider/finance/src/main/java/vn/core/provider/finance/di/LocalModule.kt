package vn.core.provider.finance.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.core.data.local.PreferenceWrapper
import vn.core.provider.finance.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class LocalModule {

    @Provides
    @Singleton
    fun providePreferenceWrapper(@ApplicationContext androidContext: Context): PreferenceWrapper =
        PreferenceWrapper(context = androidContext, name = BuildConfig.LIBRARY_PACKAGE_NAME)
}
