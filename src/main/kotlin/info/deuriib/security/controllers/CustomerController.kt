package info.deuriib.security.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Objects

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val sessionRegistry: SessionRegistry) {

    @GetMapping("insecure", "Accept=application/json")
    fun get(): String {
        return "Hello from CustomerController: INSECURE"
    }

    @GetMapping("secure", "Accept=application/json")
    fun getProtected(): String {
        return "Greetings from CustomerController: SECURE"
    }

    @GetMapping("session", "Accept=application/json")
    fun getSession(): ResponseEntity<Any> {
        var user: User? = null
        var sessionId: String = ""

        sessionRegistry.allPrincipals
                .forEach { session ->
                    if (session is User)
                        user = session

                    sessionRegistry.getAllSessions(session, false)
                            .forEach { sessionInfo ->
                                sessionId = sessionInfo.sessionId
                            }
                }

        val response = HashMap<String, Any>()
        response["response"] = "OK"
        response["sessionId"] = sessionId
        response["sessionUser"] = user as Any

        return ResponseEntity.ok(response)
    }
}