package benchmark.controller.presentations.sync

val htmlFlowTemplateIter = 123
//
// @Named
// @Path("/stocks")
// class StocksResourceBlocking @Inject constructor(
//    private val freemarkerConfig: Configuration,
//    private val pebbleEngine: PebbleEngine,
//    private val thymeleafEngine: TemplateEngine,
//    private val mustacheEngine: MustacheEngine,
//    private val velocityEngine: VelocityEngine,
//    private val stocks: StockRepository,
// ) {
//    private val pebbleView = pebbleEngine.getTemplate("templates/stock/pebble/index-pebble.html")
//    private val freemarkerView = freemarkerConfig.getTemplate("templates/stock/freemarker/index-freemarker.ftl")
//    private val trimouView = mustacheEngine.getMustache("templates/stock/mustache/index-mustache.mustache")
//    private val viewVelocity = velocityEngine.getTemplate("templates/stock/velocity/stocks-velocity.vm", "UTF-8")
//
//    /**
//     * Data models
//     */
//    private val stocksIter = stocks.findAllIterable()
//    private val stocksModelJStachio = JStachioView.StocksModel(stocksIter)
//    private val stocksModelMap: Map<String, Any> = mutableMapOf("stocks" to stocksIter)
//    private val stocksModelVelocity = VelocityContext(stocksModelMap)
//    private val stocksModelThymeleaf = Context().apply { setVariable("stocks", stocksIter) }
//
//    @GET
//    @Path("/rocker")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateRockerSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                rocker
//                    .stocks
//                    .template(stocksIter)
//                    .render { contentType, charset -> OutputStreamOutput(contentType, out, charset) }
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/jstachio")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateJStachioSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                JStachioView.stocksWrite(stocksModelJStachio, out)
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/pebble")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplatePebbleSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                pebbleView.evaluate(out.writer(), stocksModelMap)
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/freemarker")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateFreemarkerSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                freemarkerView.process(stocksModelMap, out.writer())
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/trimou")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateTrimouSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                trimouView.render(out.writer(), stocksModelMap)
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/velocity")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateVelocitySync(): Response {
//        val output =
//            StreamingOutput { out ->
//                viewVelocity.merge(stocksModelVelocity, out.writer())
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/thymeleaf")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateThymeleafSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                thymeleafEngine.process("index-thymeleaf", stocksModelThymeleaf, out.writer())
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/htmlflow")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateHtmlFlowSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                htmlFlowTemplateIter
//                    .setOut(out.writer())
//                    .write(stocksIter)
//            }
//        return Response.ok(output).build()
//    }
//
//    @GET
//    @Path("/kotlinx")
//    @Produces(MediaType.TEXT_HTML)
//    fun handleTemplateKotlinXSync(): Response {
//        val output =
//            StreamingOutput { out ->
//                kotlinXIter(out.writer(), stocksIter)
//            }
//        return Response.ok(output).build()
//    }
// }
