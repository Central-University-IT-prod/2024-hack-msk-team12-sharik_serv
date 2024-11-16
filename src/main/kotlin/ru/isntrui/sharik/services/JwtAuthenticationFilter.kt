package ru.isntrui.sharik.services

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.lang.NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthenticationFilter(
        private val jwtService: JwtService,
        private val userService: UserService
) : OncePerRequestFilter() {

    companion object {
        const val BEARER_PREFIX = "Bearer "
        const val HEADER_NAME = "Authorization"
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
            @NonNull request: HttpServletRequest,
            @NonNull response: HttpServletResponse,
            @NonNull filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HEADER_NAME)
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(BEARER_PREFIX.length)
        val username = jwtService.extractUserName(jwt)

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(username)

            if (jwtService.isTokenValid(jwt, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }
}