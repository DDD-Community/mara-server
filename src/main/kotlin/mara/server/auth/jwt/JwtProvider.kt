package com.rainbow.server.auth.jwt

import com.rainbow.server.auth.security.PrincipalDetailsService
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.util.logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtProvider(
    @Value("\${jwt.secret-key}") private val secretKey: String,
    @Value("\${jwt.access-duration-mils}") private val accessDurationMils: Long,
    private val principalDetailsService: PrincipalDetailsService,
) {
    val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())
    val log = logger()

    fun generateToken(member: Member): String {
        val now = Date(System.currentTimeMillis())
        return Jwts.builder()
            .setSubject(member.memberId.toString())
            .claim("kaKaoId", member.kaKaoId)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + accessDurationMils))
            .signWith(key)
            .compact()
    }

    fun validate(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            log.warn("JWT 오류 발생 [{}] {}", e.javaClass.simpleName, e.message)
            false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val body = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val userDetails = principalDetailsService.loadUserByUsername(userId = body.subject)
        return UsernamePasswordAuthenticationToken(
            userDetails.username,
            userDetails.password,
            userDetails.authorities,
        )
    }
}
