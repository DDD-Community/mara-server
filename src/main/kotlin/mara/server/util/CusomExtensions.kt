package mara.server.util

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
fun HttpServletRequest.getIp(): String = this.getHeader("X-Forwarded-For")
    ?: this.getHeader("Proxy-Client-IP")
    ?: this.getHeader("WL-Proxy-Client-IP")
    ?: this.getHeader("HTTP_CLIENT_IP")
    ?: this.getHeader("HTTP_X_FORWARDED_FOR")
    ?: this.remoteAddr
