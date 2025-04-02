package benchmark.webflux.router

import benchmark.repository.PresentationRepository
import benchmark.view.appendable.AppendableSink
import benchmark.view.appendable.OutputStreamSink
import benchmark.view.appendable.WriterSink
import benchmark.view.presentations.JStachioView
import benchmark.view.presentations.JStachioView.PresentationsModel
import benchmark.view.presentations.htmlFlowTemplate
import benchmark.view.presentations.htmlFlowTemplateIter
import benchmark.view.presentations.htmlFlowTemplateSuspending
import benchmark.view.presentations.htmlFlowTemplateSync
import benchmark.view.presentations.kotlinXIter
import benchmark.view.presentations.kotlinXReactive
import benchmark.view.presentations.kotlinXSync
import com.fizzed.rocker.runtime.OutputStreamOutput
import freemarker.template.Configuration
import io.pebbletemplates.pebble.PebbleEngine
import io.reactivex.rxjava3.core.BackpressureStrategy.DROP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable
import org.trimou.engine.MustacheEngine
import reactor.core.publisher.Mono

/**
 * Since JAX-RS does not support Project Reactor, we use this class to implement NIO routes using Spring WebFlux.
 */
@Component
class PresentationsRoutes(
    private val freemarkerConfig: Configuration,
    private val pebbleEngine: PebbleEngine,
    private val thymeleafEngine: TemplateEngine,
    private val mustacheEngine: MustacheEngine,
    private val velocityEngine: VelocityEngine,
    private val presentations: PresentationRepository,
) {
    /**
     * We are using next one for synchronous blocking render.
     * We need to release calling thread to proceed request handling and return Publisher<String> with HTML.
     * Using Dispatchers.Unconfined on Blocking IO will prevent Progressive Rendering.
     */
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * It executes the initial continuation of a coroutine in the current
     * call-frame and lets the coroutine resume in whatever thread.
     * Better performance but not suitable for blocking IO, only for NIO.
     * NIO will release threads to perform other task.
     */
    private val unconf = CoroutineScope(Dispatchers.Unconfined)

    /**
     * Views
     */
    private val pebbleView = pebbleEngine.getTemplate("presentations")
    private val freemarkerView = freemarkerConfig.getTemplate("templates/freemarker/index-freemarker.ftl")
    private val trimouView = mustacheEngine.getMustache("templates/trimou/presentations.trimou")
    private val viewVelocity = velocityEngine.getTemplate("templates/velocity/presentations-velocity.vm", "UTF-8")

    /**
     * Data models
     */
    private val presentationsFlux = presentations.findAllReactive()
    private val presentationsIter = presentations.findAllIterable()
    private val presentationsModelJStachio = PresentationsModel(presentationsIter)
    private val presentationsModelMap: Map<String, Any> = mutableMapOf("presentations" to presentationsIter)
    private val presentationsModelVelocity = VelocityContext(presentationsModelMap)
    private val presentationsModelThymeleaf = Context().apply { setVariable("presentations", presentationsIter) }
    private val presentationsFlow = presentations.findAllReactive().toFlowable(DROP).asFlow()
    private val presentationModelThymeleafRx = mapOf<String, Any>("presentations" to ReactiveDataDriverContextVariable(presentationsFlux, 1))

    @Bean
    fun presentationsRouter() =
        router {
        /*
         * Thymeleaf
         */
            GET("/presentations/thymeleaf") { handleTemplateThymeleaf() }
            GET("/presentations/thymeleaf/sync") { handleTemplateThymeleafSync() }
            GET("/presentations/thymeleaf/virtualSync") { handleTemplateThymeleafVirtualSync() }
        /*
         * HtmlFlow
         */
            GET("/presentations/htmlFlow") { handleTemplateHtmlFlowFromFlux() }
            GET("/presentations/htmlFlow/suspending") { handleTemplateHtmlFlowSuspending() }
            GET("/presentations/htmlFlow/sync") { handleTemplateHtmlFlowSync() }
            GET("/presentations/htmlFlow/virtualSync") { handleTemplateHtmlFlowVirtual() }
        /*
         * KotlinX
         */
            GET("/presentations/kotlinx") { handleTemplateKotlinX() } // Async non-blocking BUT returns MALL FORMED HTML
            GET("/presentations/kotlinx/sync") { handleTemplateKotlinXSync() }
            GET("/presentations/kotlinx/virtualSync") { handleTemplateKotlinXVirtualSync() }
        /*
         * Others that do NOT support data models with Asynchronous APIs.
         * Those use sync blocking approaches running on different Dispatcher and other thread pool,
         */
            GET("/presentations/rocker/sync") { handleTemplateRockerSync() }
            GET("/presentations/rocker/virtualSync") { handleTemplateRockerVirtualSync() }

            GET("/presentations/jstachio/sync") { handleTemplateJStachioSync() }
            GET("/presentations/jstachio/virtualSync") { handleTemplateJStachioVirtualSync() }

            GET("/presentations/pebble/sync") { handleTemplatePebbleSync() }
            GET("/presentations/pebble/virtualSync") { handleTemplatePebbleVirtualSync() }

            GET("/presentations/freemarker/sync") { handleTemplateFreemarkerSync() }
            GET("/presentations/freemarker/virtualSync") { handleTemplateFreemarkerVirtualSync() }

            GET("/presentations/trimou/sync") { handleTemplateTrimouSync() }
            GET("/presentations/trimou/virtualSync") { handleTemplateTrimouVirtualSync() }

            GET("/presentations/velocity/sync") { handleTemplateVelocitySync() }
            GET("/presentations/velocity/virtualSync") { handleTemplateVelocityVirtualSync() }
        }

    private fun handleTemplateRockerSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                scope.launch {
                    rocker
                        .presentations
                        .template(presentationsIter)
                        .render { contentType, charset -> OutputStreamOutput(contentType, it, charset) }
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateRockerVirtualSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                Thread.startVirtualThread {
                    rocker
                        .presentations
                        .template(presentationsIter)
                        .render { contentType, charset -> OutputStreamOutput(contentType, it, charset) }
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateJStachioSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                scope.launch {
                    JStachioView.presentationsWrite(presentationsModelJStachio, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateJStachioVirtualSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                Thread.startVirtualThread {
                    JStachioView.presentationsWrite(presentationsModelJStachio, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplatePebbleSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    pebbleView.evaluate(it, presentationsModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplatePebbleVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    pebbleView.evaluate(it, presentationsModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateFreemarkerSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    freemarkerView.process(presentationsModelMap, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateFreemarkerVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    freemarkerView.process(presentationsModelMap, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateTrimouSync(): Mono<ServerResponse> {
        val out =
            AppendableSink().also {
                scope.launch {
                    trimouView.render(it, presentationsModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateTrimouVirtualSync(): Mono<ServerResponse> {
        val out =
            AppendableSink().also {
                Thread.startVirtualThread {
                    trimouView.render(it, presentationsModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateVelocitySync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    viewVelocity.merge(presentationsModelVelocity, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateVelocityVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    viewVelocity.merge(presentationsModelVelocity, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateThymeleafSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    thymeleafEngine.process("index-thymeleaf", presentationsModelThymeleaf, it)
                    it.close()
                }
            }

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateThymeleafVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    thymeleafEngine.process("index-thymeleaf", presentationsModelThymeleaf, it)
                    it.close()
                }
            }

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateThymeleaf(): Mono<ServerResponse> {
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("index-thymeleaf", presentationModelThymeleafRx)
    }

    private fun handleTemplateHtmlFlowSync(): Mono<ServerResponse> {
        /*
         * We need another co-routine in another thread (this one is blocking IO) to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        val view =
            AppendableSink().also {
                scope.launch {
                    htmlFlowTemplateSync
                        .setOut(it)
                        .write(presentationsFlux)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowVirtual(): Mono<ServerResponse> {
        val view =
            AppendableSink().also {
                Thread.startVirtualThread {
                    htmlFlowTemplateIter
                        .setOut(it)
                        .write(presentationsIter)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowFromFlux(): Mono<ServerResponse> {
        val view =
            AppendableSink().also { sink ->
                htmlFlowTemplate
                    .writeAsync(sink, presentationsFlux)
                    .thenAccept { sink.close() }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowSuspending(): Mono<ServerResponse> {
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         * Here we are using Unconfined running in same therad and avoiding context switching.
         * That's ok since we are NOT blocking on htmlFlowTemplateSuspending.
         */
        val view =
            AppendableSink().also {
                unconf.launch {
                    htmlFlowTemplateSuspending
                        .write(it, presentationsFlow)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinXSync(): Mono<ServerResponse> {
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        val view =
            AppendableSink().also {
                scope.launch {
                    kotlinXSync(it, presentationsFlux)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinX(): Mono<ServerResponse> {
        val view =
            AppendableSink().also {
                kotlinXReactive(it, presentationsFlux)
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinXVirtualSync(): Mono<ServerResponse> {
        val view =
            AppendableSink().also {
                Thread.startVirtualThread {
                    kotlinXIter(it, presentationsIter)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }
}
