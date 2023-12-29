package com.rainbow.server.auth

import com.rainbow.server.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class KakaoApiClient(
    private val restTemplate: RestTemplate,
    @Value("\${oauth.kakao.url.auth}")
    private val authUrl: String,

    @Value("\${oauth.kakao.url.api}")
    private val apiUrl: String,

    @Value("\${oauth.kakao.client-id}")
    private val clientId: String,

    @Value("\${oauth.kakao.client-secret}")
    private val secret: String,

    @Value("\${oauth.kakao.app-admin-key}")
    private val appAdminKey: String,
) {

    val log = logger()

    fun getRedirectUri(status: String): String {
        val os = System.getProperty("os.name")
        log.info("OS : {}", os)
//        if (os.contains("Mac") || os.contains("Windows")) return "http://localhost:8080/member/login"
        if (status == "local")return "http://localhost:3000/member/kakao"
        if (status == "prod") return "http://www.buybye.kr/member/kakao"
        return "http://localhost:8080/member/login"
    }

    fun requestAccessToken(code: String, status: String): String {
        val url = "$authUrl/oauth/token"
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val body: MultiValueMap<String, String> = LinkedMultiValueMap()
        body.add("code", code)
        body.add("grant_type", "authorization_code")
        body.add("client_id", clientId)
        body.add("client_secret", secret)
        body.add("redirect_uri", getRedirectUri(status))

        val request = HttpEntity(body, httpHeaders)

        val response = restTemplate.postForObject(url, request, KakaoTokens::class.java)
            ?: throw IllegalStateException("KakaoTokens response is null")
        return response.accessToken
    }

    fun requestOauthInfo(accessToken: String): KakaoInfoResponse {
        val url = "$apiUrl/v2/user/me"

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        httpHeaders.set("Authorization", "Bearer $accessToken")

        val body = LinkedMultiValueMap<String, String>()
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]")

        val request = HttpEntity(body, httpHeaders)

        return restTemplate.postForObject(url, request, KakaoInfoResponse::class.java)
            ?: throw IllegalStateException("KakaoInfoResponse is null")
    }

    fun logout(kaKaoId: Long): Boolean {
        val url = "$apiUrl/v1/user/logout"

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_FORM_URLENCODED
        httpHeaders.set("Authorization", "KakaoAK $appAdminKey")

        val body = LinkedMultiValueMap<String, String>()
        body.add("target_id_type", "user_id")
        body.add("target_id", kaKaoId.toString())
        val request = HttpEntity(body, httpHeaders)
        val response = restTemplate.postForObject(url, request, KaKaoUserLogout::class.java)
            ?: throw IllegalStateException("KakaoInfoResponse is null")

        return response.id == kaKaoId
    }
}
