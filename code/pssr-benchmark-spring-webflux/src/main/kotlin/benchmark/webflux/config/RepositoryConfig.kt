package benchmark.webflux.config

import benchmark.repository.PresentationRepository
import benchmark.repository.PresentationRepositoryMem
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RepositoryConfig {
    @Bean
    open fun presentationRepository(): PresentationRepository {
        val timeout = System.getProperty("benchTimeout")?.toLongOrNull() ?: 300L
        return PresentationRepositoryMem(timeout)
    }
}
