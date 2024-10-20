package vn.core.provider.finance.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import vn.core.provider.finance.BuildConfig
import vn.core.data.di.AnoHttpAuthenticatorInterceptor
import vn.core.data.di.AnoHttpLoggingInterceptor
import vn.core.data.local.PreferenceWrapper
import vn.core.provider.finance.Configs
import vn.core.provider.finance.network.HttpAuthenticatorInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    @Singleton
    @AnoHttpAuthenticatorInterceptor
    fun bindHttpAuthenticatorInterceptor(preferenceWrapper: PreferenceWrapper): HttpAuthenticatorInterceptor {
        return HttpAuthenticatorInterceptor(preferenceWrapper)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @AnoHttpLoggingInterceptor httpLoggingInterceptor: HttpLoggingInterceptor,
        @AnoHttpAuthenticatorInterceptor httpAuthenticatorInterceptor: HttpAuthenticatorInterceptor,
        preferenceWrapper: PreferenceWrapper,
        cache: Cache,
    ): OkHttpClient {
        val builder =
            OkHttpClient.Builder().readTimeout(Configs.TimeOut.TIMEOUT_READ_SECOND, TimeUnit.SECONDS)
                .connectTimeout(Configs.TimeOut.TIMEOUT_CONNECT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(Configs.TimeOut.TIMEOUT_WRITE_SECOND, TimeUnit.SECONDS)
                .addNetworkInterceptor(
                    Interceptor { chain ->
                        var request = chain.request()
                        val builder = request.newBuilder()
                        val token =
                            preferenceWrapper.getString(Configs.SharePreference.KEY_AUTH_TOKEN, "")
                        if (token.isNotEmpty()) {
                            builder.addHeader("Authorization", "Bearer $token")
                        }
                        request = builder.build()
                        chain.proceed(request)
                    },
                ).authenticator(httpAuthenticatorInterceptor)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
        } else {
            builder.cache(cache)
        }
        return builder.build()
    }
}