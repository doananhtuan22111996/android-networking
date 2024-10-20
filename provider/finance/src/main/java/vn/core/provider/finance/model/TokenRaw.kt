package vn.core.provider.finance.model

import vn.core.data.model.BaseRaw
import vn.core.domain.BaseModel
import vn.core.provider.finance.Configs.EMPTY_STRING

data class TokenRaw(val token: String? = EMPTY_STRING, val refreshToken: String? = EMPTY_STRING) :
    BaseRaw() {

    override fun raw2Model(): BaseModel =
        TokenModel(token = token ?: EMPTY_STRING, refreshToken = refreshToken ?: EMPTY_STRING)
}