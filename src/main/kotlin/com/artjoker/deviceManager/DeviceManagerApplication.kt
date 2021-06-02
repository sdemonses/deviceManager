package com.artjoker.deviceManager

import com.artjoker.deviceManager.configuration.BACKOFFICE
import com.artjoker.deviceManager.configuration.CLIENT
import com.artjoker.deviceManager.configuration.JWTAuthorizationFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.time.Clock


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class deviceManagerApplication

fun main(args: Array<String>) {
    runApplication<deviceManagerApplication>(*args)
}

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
internal class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .addFilterAfter(JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "*/v1/devices").hasRole(BACKOFFICE)
            .antMatchers(HttpMethod.POST, "*/v1/customers/*/devices").hasRole(CLIENT)
            .antMatchers(HttpMethod.DELETE, "*/v1/customers/*/devices/*").hasRole(CLIENT)
            .antMatchers(HttpMethod.PUT, "*/v1/customers/*/devices/*").hasRole(CLIENT)
            .anyRequest().authenticated()
    }
}


@Configuration
class Clocks {
    @Bean
    fun clock(): Clock = Clock.systemDefaultZone()
}