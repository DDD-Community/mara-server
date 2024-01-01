package mara.server.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mara.server.common.CommonResponse
import mara.server.util.getIp
import mara.server.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {
    val log = logger()
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AuthenticationException,
    ) {
        log.warn("{} {} {} (401) - {}", request.getIp(), request.method, request.requestURI, e.message)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.status = HttpStatus.UNAUTHORIZED.value()
        val body = objectMapper.writeValueAsString(
            CommonResponse<Any>(
                message = "authentication failed, please recheck JWT.",
            ),
        )
        response.writer.write(body)
    }
}

@Component
class JwtAccessDeniedHandler(private val objectMapper: ObjectMapper) : AccessDeniedHandler {
    val log = logger()
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AccessDeniedException,
    ) {
        log.warn("(403) Access is not granted = {}", e.message)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.status = HttpStatus.FORBIDDEN.value()
        val body = objectMapper.writeValueAsString(
            CommonResponse<Any>(
                message = "access denied, user is not granted.",
            ),
        )
        response.writer.write(body)
    }
}
