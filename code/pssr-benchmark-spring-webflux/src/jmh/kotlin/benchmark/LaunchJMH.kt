package benchmark

import benchmark.webflux.Launch
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.TearDown
import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.net.URI
import java.util.concurrent.TimeUnit

// -i 4 iterations
// -wi 4 warmup iterations
// -f 1 fork
// -r 2 run each iteration for 2 seconds
// -w 2 run each warmup iteration for 2 seconds.
// -t 8 worker threads
@SpringBootTest
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@org.openjdk.jmh.annotations.Fork(value = 1)
@org.openjdk.jmh.annotations.State(Scope.Benchmark) // java -jar target/template-engines.jar -i 4 -wi 4 -f 1 -r 2 -w 2 -t 8 -p route=/rocker/sync,/thymeleaf,/htmlFlow,/kotlinx
open class LaunchJMH {
    @org.openjdk.jmh.annotations.Param(
        "/presentations/rocker/sync",
        "/presentations/thymeleaf/sync",
        "/presentations/htmlFlow/sync",
        "/presentations/kotlinx/sync",
        "/presentations/thymeleaf",
        "/presentations/thymeleaf/virtualSync",
        "/presentations/htmlFlow",
        "/presentations/htmlFlow/suspending",
        "/presentations/htmlFlow/virtualSync",
        "/presentations/kotlinx/virtualSync",
        "/stocks/rocker/sync",
        "/stocks/thymeleaf/sync",
        "/stocks/htmlFlow/sync",
        "/stocks/kotlinx/sync",
        "/stocks/thymeleaf",
        "/stocks/thymeleaf/virtualSync",
        "/stocks/htmlFlow",
        "/stocks/htmlFlow/suspending",
        "/stocks/htmlFlow/virtualSync",
        "/stocks/kotlinx/virtualSync",
    )
    lateinit var route: String

    companion object {
        var context: ConfigurableApplicationContext? = null
        var webTestClient: WebTestClient? = null
    }

    @Setup(Level.Trial)
    @Synchronized
    fun startupSpring() {
        try {
            if (context == null) {
                System.setProperty("benchTimeout", "10")
                context = SpringApplication.run(Launch::class.java)
                webTestClient =
                    WebTestClient
                        .bindToApplicationContext(context!!)
                        .build()
            }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @TearDown(Level.Trial)
    @Synchronized
    fun shutdownSpring() {
        try {
            if (context != null) {
                SpringApplication.exit(context)
                context = null
                System.clearProperty("benchTimeout")
            }
        } catch (e: Exception) {
            // Force JMH crash
            throw RuntimeException(e)
        }
    }

    @Benchmark
    fun benchmarkTemplate(): String {
        return String(
            webTestClient!!.get()
                .uri(URI.create(route))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody().returnResult().responseBody!!,
        )
    }
}
