package ru.isntrui.sharik.configurations

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import ru.isntrui.sharik.services.JwtAuthenticationFilter
import ru.isntrui.sharik.services.UserService

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val userService: UserService
) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors(Customizer { cors: CorsConfigurer<HttpSecurity?>? ->
                cors!!.configurationSource(CorsConfigurationSource { request: HttpServletRequest? ->
                    val corsConfiguration = CorsConfiguration()
                    corsConfiguration.allowedOriginPatterns = mutableListOf<String?>("*")
                    corsConfiguration.allowedMethods = mutableListOf<String?>(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                    )
                    corsConfiguration.allowedHeaders = mutableListOf<String?>("*")
                    corsConfiguration.allowCredentials = true
                    corsConfiguration
                })
            })
            .csrf(Customizer { obj: CsrfConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .authorizeHttpRequests { request ->
                request
                    .requestMatchers("/docs/**", "/api/auth/**", "/actuator/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement(Customizer { manager: SessionManagementConfigurer<HttpSecurity?>? ->
                manager!!.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            })
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userService.userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}