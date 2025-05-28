package benchmark.controller.presentations.reactive

import benchmark.model.Presentation
import benchmark.model.Stock
import benchmark.repository.PresentationRepository
import benchmark.view.appendable.AppendableSink
import benchmark.view.presentations.PresentationsHtmlFlow
import io.quarkus.qute.CheckedTemplate
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import io.smallrye.mutiny.Multi
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/presentations/reactive")
class PresentationsResourceReactive
    @Inject
    constructor(
        private val presentations: PresentationRepository,
    ) {
        private val presentationsIter = presentations.findAllIterable()
        private val presentationsFlux = presentations.findAllReactive()

        @Location("qute/presentations")
        lateinit var template: Template

        @GET
        @Path("/qute")
        @Produces(MediaType.TEXT_HTML)
        fun getQute(): Multi<String> {
            return template.data("presentations", presentationsIter).createMulti()
        }

        @GET
        @Path("/htmlFlow")
        @Produces(MediaType.TEXT_HTML)
        fun getHtmlFlow(): Multi<String> {
            val view = AppendableMulti().also { sink ->
                    PresentationsHtmlFlow.htmlFlowTemplate
                        .writeAsync(sink, presentationsFlux)
                        .thenAccept { sink.close() }
                }
            return view.toMulti()
        }

    }

