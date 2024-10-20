package vn.core.provider.finance.model

import vn.core.domain.BaseModel

data class TokenModel(val token: String, val refreshToken: String) : BaseModel()