package com.rainbow.server.auth
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoInfoResponse(
    @JsonProperty("kakao_account")
    var kakaoAccount: KakaoAccount = KakaoAccount(),

    @JsonProperty("kakao_profile")
    var kakaoProfile: KakaoProfile = KakaoProfile(),

    @JsonProperty("id")
    var id: Long,

) {
    val email: String
        get() = kakaoAccount.email

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoAccount(
        var profile: KakaoProfile = KakaoProfile(),
        var email: String = "",
        var ageRange: String = "",
        var gender: String = "",
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoProfile(
        var nickname: String = "",
    )
}
