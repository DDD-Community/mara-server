package com.rainbow.server.auth.security

import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

class PrincipalDetails(private val member: Member) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        authorities.add { member.role.toString() }
        return authorities
    }

    override fun getPassword(): String = member.password

    // 유저를 식별할 수 있는 고유값을 넘겨줘야 함 (DB PK)
    override fun getUsername(): String = member.memberId.toString()
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

@Service
class PrincipalDetailsService(
    private val memberRepository: MemberRepository,
) : UserDetailsService {

    override fun loadUserByUsername(userId: String) = PrincipalDetails(
        memberRepository.findById(userId.toLong())
            .orElseThrow(),
    )
}

// principal 에는 username (User PK 값) 이 문자열로 들어있다
fun getCurrentLoginUserId() = (
    SecurityContextHolder
        .getContext()
        .authentication
        .principal as String
    )
    .toLong()
