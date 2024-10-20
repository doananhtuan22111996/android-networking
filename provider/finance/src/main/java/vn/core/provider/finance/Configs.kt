package vn.core.provider.finance

object Configs {

    const val EMPTY_STRING = ""
    const val MAIN_DOMAIN = "https://6aa81800-aafe-4965-bfb1-687428ec2b3d.mock.pstmn.io"

    object TimeOut {
        const val TIMEOUT_READ_SECOND = 15L
        const val TIMEOUT_CONNECT_SECOND = 15L
        const val TIMEOUT_WRITE_SECOND = 15L
    }

    object SharePreference {
        const val KEY_AUTH_TOKEN = "key_auth_token"
        const val KEY_AUTH_REFRESH_TOKEN = "key_auth_refresh_token"
    }
}