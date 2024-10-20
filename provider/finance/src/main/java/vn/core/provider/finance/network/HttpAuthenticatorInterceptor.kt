package vn.core.provider.finance.network

import com.google.gson.Gson
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okio.use
import timber.log.Timber
import vn.core.data.local.PreferenceWrapper
import vn.core.data.model.ObjectResponse
import vn.core.provider.finance.Configs
import vn.core.provider.finance.model.TokenRaw
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

class HttpAuthenticatorInterceptor(
    private val preferenceWrapper: PreferenceWrapper
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Timber.d("Request URL: ${response.request.url}")
        val requestUrl = response.request.url.toString()
        val ignored = requestUrl.contains("login")
                || requestUrl.contains("refreshToken")
                || requestUrl.contains("sign-up")
        if (response.code == HTTP_UNAUTHORIZED && !ignored) {
            try {
                val refreshToken =
                    preferenceWrapper.getString(Configs.SharePreference.KEY_AUTH_REFRESH_TOKEN, "")
                refreshToken(refreshToken)
                val newToken =
                    preferenceWrapper.getString(Configs.SharePreference.KEY_AUTH_TOKEN, "")
                val builder = response.request.newBuilder()
                if (newToken.isNotEmpty()) {
                    // TODO change Author method depend by context. builder.addHeader("Authorization", "Bearer $newToken")
                    builder.addHeader("Authorization", newToken)
                }
                return builder.build()
            } catch (e: Exception) {
                Timber.e("Refresh Token Somethings Wrong: ${e.message}")
                return null
            }
        }
        return null
    }

    private fun refreshToken(refreshToken: String, onCallBack: () -> Unit = {}) {
        val request = Request.Builder().url("${Configs.MAIN_DOMAIN}/refreshToken")
            .post(refreshToken.toRequestBody()).build()
        OkHttpClient().newBuilder().build().newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val objResponse =
                    Gson().fromJson(response.body.string(), ObjectResponse::class.java)
                val tokenRaw = objResponse.data as? TokenRaw
                Timber.d("Refresh Token Success: ${tokenRaw?.refreshToken}")
                preferenceWrapper.saveString(
                    Configs.SharePreference.KEY_AUTH_TOKEN, tokenRaw?.token ?: ""
                )
                preferenceWrapper.saveString(
                    Configs.SharePreference.KEY_AUTH_REFRESH_TOKEN, tokenRaw?.refreshToken ?: ""
                )
                onCallBack()
            } else {
                Timber.e("Refresh Token Failure: ${response.message}")
            }
        }
    }
}