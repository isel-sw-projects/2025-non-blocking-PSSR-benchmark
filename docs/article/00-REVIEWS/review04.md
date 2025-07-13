The paper examines how Java Virtual Threads (introduced in Java 21) enable
non-blocking execution of blocking I/O operations, allowing the reuse of
traditional web template engines for Progressive Server-Side Rendering (PSSR).
Through extensive benchmarks using frameworks like Spring WebFlux, Spring MVC,
and Quarkus, the authors show that virtual threads can deliver performance
comparable to reactive models while simplifying development by maintaining
synchronous programming styles.

Here are some comments that should be taken into consideration to improve the
paper:

Although the phrase "blocking I/O" is used several times, the abstract is
well-framed. Impact would be enhanced by more precise wording.

---

The paper may be difficult to read because of its lengthy, dense paragraphs and
technical jargon. Clarity would be increased by dividing the text into shorter
sections/subsections. Excellent discussion of previous approaches such as
Reactive Streams, Thymeleaf, and HTMLFlow. Citing more recent research on
coroutine-based SSR or structured concurrency beyond Java would enhance the
related work.

---

REPLY: we have included now new references on structured concurrency and virtual threads.

---

- Is it really necessary to include "more recent research on coroutine-based SSR or structured concurrency beyond Java"? We include "Progressive Server-Side Rendering with Suspendable Web Templates" from 2024. Also I don't know how relevant it is to discuss other platforms when our focus in on the Java ecosystem.

---

Benchmarks are run only on controlled, synthetic data (e.g., Stock and
Presentation models). Including real-world or more complex data could strengthen
the evaluation.

---

Answer: same as reviewer 3

---

For the experimental setup, the use of JMeter and GitHub Actions is practical,
but more system specs (latency/bandwidth details) would add value.

---

Answer: same as reviewer 1

---

While discussed, HTML safety enforcement in template engines is not evaluated in
the benchmark section. A brief empirical analysis could be helpful.

---

REPLY: What more beyond already explained in 2.1.4. HTML safety ?????
We are only mentione that as a core characteristic that is hard to be provided
by external DSLs. The DSLs that we showed support HTML safety are internal DSLs
that enforce it at compile-time, it does not have any impact on performance.

---

This study only looks at backend throughput (RPS). It would be more
realistically relevant to include perceived latency (e.g., Time to First Byte,
First Contentful Paint).

---

REPLY: Already dealed in previsous work and now we include reference in Intro.

---

Tables and figures are informative; however, they
require clearer legends and captions. Axis labels and contextual explanations
are absent from certain graphs.

---

REPLY: Same as reviewer 2, we have improved the captions and added legend labels.
