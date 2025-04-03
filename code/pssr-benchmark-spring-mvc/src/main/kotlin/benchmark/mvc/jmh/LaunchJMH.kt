package benchmark.mvc.jmh

import benchmark.mvc.Launch
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.springframework.boot.SpringApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.net.URI
import java.time.Duration
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Benchmark)
// java -jar target/template-engines.jar -i 4 -wi 4 -f 1 -r 2 -w 2 -t 8 -p route=/rocker/sync,/thymeleaf,/htmlFlow,/kotlinx
//
// -i 4 iterations
// -wi 4 warmup iterations
// -f 1 fork
// -r 2 run each iteration for 2 seconds
// -w 2 run each warmup iteration for 2 seconds.
// -t 8 worker threads
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LaunchJMH {
    @Param(
        "/presentations/rocker",
        "/presentations/jstachio",
        "/presentations/pebble",
        "/presentations/freemarker",
        "/presentations/trimou",
        "/presentations/velocity",
        "/presentations/thymeleaf",
        "/presentations/htmlFlow",
        "/presentations/kotlinx",
    )
    lateinit var route: String

    companion object {
        var context: ConfigurableApplicationContext? = null
        lateinit var webTestClient: WebTestClient
    }

    @LocalServerPort
    var port: Int = 0

    @Setup(Level.Trial)
    @Synchronized
    fun startupSpring() {
        try {
            if (context == null) {
                context = SpringApplication.run(Launch::class.java)
                webTestClient =
                    WebTestClient.bindToServer()
                        .baseUrl("http://localhost:$port") // Adjust base URL if needed
                        .responseTimeout(Duration.ofMinutes(1))
                        .build()
            }
        } catch (e: Exception) {
            // Force JMH crash
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
            }
        } catch (e: Exception) {
            // Force JMH crash
            throw RuntimeException(e)
        }
    }

    @Benchmark
    fun benchmarkTemplate(): String {
        return String(
            webTestClient.get()
                .uri(URI.create(route))
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus()
                .isOk
                .expectBody().returnResult().responseBody!!,
        )
    }
}
