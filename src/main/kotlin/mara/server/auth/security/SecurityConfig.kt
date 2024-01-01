package mara.server.auth.security

import mara.server.auth.jwt.JwtAccessDeniedHandler
import mara.server.auth.jwt.JwtAuthenticationEntryPoint
import mara.server.auth.jwt.JwtFilter
import mara.server.auth.jwt.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.StrictHttpFirewall

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
) {

    @Bean
    fun setDefaultFirewall(): StrictHttpFirewall {
        val firewall = StrictHttpFirewall()
        firewall.setAllowSemicolon(true)
        firewall.setAllowUrlEncodedDoubleSlash(true)
        return firewall
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityConfigurerAdapter<*, *> {
//        http.csrf { it.disable() }
//            .formLogin { it.disable() }
//            .httpBasic{ it.disable() }
//            .headers { it.frameOptions { it.sameOrigin() } }
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            .accessDeniedHandler(jwtAccessDeniedHandler)
//            .and()
//            .apply(jwtFilterConfigurer())
//            .and()
//            .authorizeRequests()
//            .antMatchers(
//                "/member/**",
//                "**",
//                "/**",
//                "/**/**",
//            ).permitAll()
//            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//            .antMatchers(
//                "/docs/**",
//                "/favicon.ico",
//                "/v2/api-docs",
//                "/v3/api-docs",
//                "/configuration/ui",
//                "/swagger-resources/**",
//                "/configuration/security",
//                "/swagger-ui.html",
//                "/swagger-ui/#",
//                "/webjars/**",
//                "/swagger/**",
//                "/swagger-ui/**",
//                "/",
//                "/csrf",
//                "/error",
//            ).permitAll()
//            .anyRequest()
//            .authenticated()
//
//        return JwtSecurityConfig(jwtProvider)
//    }
// }

    class JwtSecurityConfig(private val jwtProvider: JwtProvider) :
        SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
        override fun configure(http: HttpSecurity) {
            http.addFilterBefore(
                JwtFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter::class.java,
            )
        }
    }
}
