package benchmark.mvc.config

import benchmark.controller.presentations.sync.PresentationsResourceBlocking
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletProperties
import org.springframework.stereotype.Component

@Component
class JerseyConfig : ResourceConfig() {
    init {
        register(PresentationsResourceBlocking::class.java)
        property(ServletProperties.FILTER_FORWARD_ON_404, true)
    }
}
