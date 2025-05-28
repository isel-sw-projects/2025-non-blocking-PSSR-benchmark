package benchmark.controller.presentations.reactive

import io.smallrye.mutiny.Multi
import java.io.Closeable
import io.smallrye.mutiny.subscription.MultiEmitter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class AppendableMulti : Appendable, Closeable {

    @Volatile
    private var emitter: MultiEmitter<in String>? = null

    private val queue = ConcurrentLinkedQueue<String>()
    private val closed = AtomicBoolean(false)
    private val emitting = AtomicBoolean(false)

    fun toMulti(): Multi<String> {
        return Multi.createFrom().emitter { em ->
            emitter = em
            drain()
        }
    }

    override fun append(csq: CharSequence): Appendable {
        val item = csq.toString()
        if (emitter != null && !closed.get()) {
            if (tryEmit(item)) {
                // no-op
            } else {
                queue.offer(item)
                drain()
            }
        } else {
            queue.offer(item)
        }
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int): Appendable {
        return append(csq.subSequence(start, end))
    }

    override fun append(c: Char): Appendable {
        return append(c.toString())
    }

    override fun close() {
        closed.set(true)
        drain()
    }

    private fun tryEmit(item: String): Boolean {
        if (emitting.compareAndSet(false, true)) {
            try {
                emitter?.emit(item)
                return true
            } catch (ex: Throwable) {
                return false
            } finally {
                emitting.set(false)
            }
        }
        return false
    }

    private fun drain() {
        if (!emitting.compareAndSet(false, true)) {
            return
        }
        try {
            val em = emitter ?: return
            do {
                while (true) {
                    val item = queue.poll() ?: break
                    em.emit(item)
                }
                if (closed.get() && queue.isEmpty()) {
                    em.complete()
                    break
                }
            } while (queue.isNotEmpty() && !closed.get())
        } finally {
            emitting.set(false)
        }
    }
}
