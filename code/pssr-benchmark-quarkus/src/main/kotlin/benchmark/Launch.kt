package benchmark

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain

@QuarkusMain
class App : QuarkusApplication {
    override fun run(args: Array<String>): Int {
        println("Starting Quarkus Application...")
        Quarkus.waitForExit()
        return 0
    }
}

fun main(args: Array<String>) {
    Quarkus.run(App::class.java, *args)
}
