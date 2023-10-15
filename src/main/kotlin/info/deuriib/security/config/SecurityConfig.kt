package info.deuriib.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests {
            it
                    .requestMatchers("/api/customers/insecure").permitAll()
                    .anyRequest().authenticated()
        }
                .formLogin {
                    it
                            .successHandler(successHandler())
                            .permitAll()
                }
                .sessionManagement {
                    it
                            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ALWAYS, IF_REQUIRED, NEVER, STATELESS
                            .invalidSessionUrl("/login")
                            .maximumSessions(1)
                            .expiredUrl("/login") // default /login?expired=true
                            .sessionRegistry(sessionRegistry())

                    it.sessionFixation().migrateSession()
                }
                .httpBasic { }
                .build()
    }

    @Bean
    fun sessionRegistry(): SessionRegistry {
        return SessionRegistryImpl()
    }

    fun successHandler(): AuthenticationSuccessHandler {
        // request, response, authentication
        return AuthenticationSuccessHandler { _, response, _ ->
            response.sendRedirect("/api/customers/secure")
        }
    }
}