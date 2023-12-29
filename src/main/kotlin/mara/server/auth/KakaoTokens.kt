package com.rainbow.server.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoTokens(
    @JsonProperty("access_token")
    var accessToken: String = "",

    @JsonProperty("token_type")
    var tokenType: String = "",

    @JsonProperty("refresh_token")
    var refreshToken: String = "",

    @JsonProperty("expires_in")
    var expiresIn: String = "",

    @JsonProperty("refresh_token_expires_in")
    var refreshTokenExpiresIn: String = "",

    @JsonProperty("scope")
    var scope: String = "",
)
