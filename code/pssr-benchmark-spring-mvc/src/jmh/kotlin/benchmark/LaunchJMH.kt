package benchmark

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.concurrent.TimeUnit

// java -Dspring.virtual.threads.enabled=true -jar build/libs/pssr-benchmark-spring-mvc-1.0-SNAPSHOT-jmh.jar -i 4 -wi 4 -f 1 -r 2 -w 2 -t 8
// -i 4 iterations
// -wi 4 warmup iterations
// -f 1 fork
// -r 2 run each iteration for 2 seconds
// -w 2 run each warmup iteration for 2 seconds.
// -t 8 worker threads
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Benchmark)
@SpringBootTest
open class LaunchJMH {
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
        val restTemplate = RestTemplate()
        val baseUrl = "http://localhost:8080"
    }

//    @Setup(Level.Trial)
//    @Synchronized
//    fun startupSpring() {
//        try {
//            if (context == null) {
//                System.setProperty("benchTimeout", "10")
//                context = SpringApplication.run(Launch::class.java)
//            }
//        } catch (e: Exception) {
//            // Force JMH crash
//            throw RuntimeException(e)
//        }
//    }
//
//    @TearDown(Level.Trial)
//    @Synchronized
//    fun shutdownSpring() {
//        try {
//            if (context != null) {
//                SpringApplication.exit(context)
//                context = null
//            }
//        } catch (e: Exception) {
//            // Force JMH crash
//            throw RuntimeException(e)
//        }
//    }

    @Benchmark
    fun benchmarkTemplate(): String {
        val uri = URI.create("$baseUrl$route")
        return restTemplate.getForObject(uri, String::class.java) ?: ""
    }
}
