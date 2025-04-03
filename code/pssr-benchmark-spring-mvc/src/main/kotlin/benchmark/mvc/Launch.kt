package benchmark.mvc

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpFilter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component

@SpringBootApplication
@ComponentScan(basePackages = ["benchmark.controller.presentations.sync", "benchmark.repository", "benchmark.mvc"])
open class Launch {
    companion object {
        private val logger = LoggerFactory.getLogger(Launch::class.java)
    }

    @Bean
    open fun beanPostProcessor() =
        object : BeanPostProcessor {
            override fun postProcessAfterInitialization(
                bean: Any,
                beanName: String,
            ): Any {
                val beanPackage = bean::class.java.packageName
                if (beanPackage.startsWith("benchmark")) {
                    logger.info("Bean $beanName of package $beanPackage initialized")
                }
                return bean
            }
        }
}

@Component
class MyFilter : HttpFilter() {
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?,
    ) {
        response?.bufferSize = 8
        chain?.doFilter(request, response)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Launch::class.java, *args)
}
