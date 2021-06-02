package com.artjoker.deviceManager.configuration

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val PREFIX = "Bearer "
private const val SECRET = "secret"

class JWTAuthorizationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        try {
            if (checkJWTToken(request)) {
                val claims = validateToken(request)
                if (claims["role"] != null) {
                    setUpSpringAuthentication(claims)
                } else {
                    SecurityContextHolder.clearContext()
                }
            } else {
                SecurityContextHolder.clearContext()
            }
            chain.doFilter(request, response)
        } catch (e: JwtException) {
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.message)
            return
        }
    }

    private fun validateToken(request: HttpServletRequest): Claims {
        val jwtToken = request.getHeader(AUTHORIZATION).replace(PREFIX, "")
        return Jwts.parser().setSigningKey(SECRET.toByteArray()).parseClaimsJws(jwtToken).body
    }

    private fun setUpSpringAuthentication(claims: Claims) {
        val auth = UsernamePasswordAuthenticationToken(
            claims.subject, null,
            listOf(SimpleGrantedAuthority(claims["role"] as String))
        )
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun checkJWTToken(request: HttpServletRequest) =
        request.getHeader(AUTHORIZATION).run {
            this != null && this.startsWith(PREFIX)
        }

}
