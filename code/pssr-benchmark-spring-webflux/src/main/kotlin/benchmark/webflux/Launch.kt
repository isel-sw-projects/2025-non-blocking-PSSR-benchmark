package benchmark.webflux

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
open class Launch {
    @ComponentScan(
        basePackages =
            ["benchmark.controller.presentations.sync", "benchmark.repository", "benchmark.webflux"],
    )
    @EnableScheduling
    open class App {
        companion object {
            private val logger = LoggerFactory.getLogger(App::class.java)
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
}

fun main(args: Array<String>) {
    SpringApplication.run(Launch::class.java, *args)
}
