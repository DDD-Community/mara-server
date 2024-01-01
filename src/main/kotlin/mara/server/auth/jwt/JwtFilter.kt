package mara.server.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    private fun HttpServletRequest.getToken(): String? {
        val rawToken = this.getHeader("Authorization")
        return if (rawToken != null && rawToken.startsWith("Bearer"))
            rawToken.replace("Bearer ", "")
        else null
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val jwt = request.getToken()

        if (jwt != null && jwtProvider.validate(jwt)) {
            SecurityContextHolder.getContext().authentication =
                jwtProvider.getAuthentication(jwt)
        }
        filterChain.doFilter(request, response)
    }
}
