The article has a good topic with the application of Java Virtual Threads to
PSSR, but the introduction is overly broad and does not clearly articulate the
research problem or novelty compared to existing work.

---

REPLY: Why ??? All those aspectes are explained.

The use of multiple rendering frameworks is interesting, but the research design
is not clear - especially in how the comparisons are disentangled from
confounding variables such as inner template engine optimizations.

---

REPLY: Why ???

The process of the benchmark section is not well described to enable
reproducibility. For example, specific template engine configurations and how
asynchronous models are always simulated should be explained.

---

ANSWER: TODO - Explain configurations.

---

Done - Added template engine configurations mention.
We already explain that we use Observable to simulate asynchronous models with an interleaving
of 1 millisecond between elements to simulate I/O operations.

-

There are no references to figures within the main text, which creates an issue
in tracking the results. For example, in Section 5.1, throughput trends are
discussed by the text without direct reference to Figure 2.

---

REPLY - No sense. The figures are referenced.

The distinction among the "Virtual", "Reactive", and "Suspendable" execution
strategies is useful, but more precisely the authors should tell us why engines
were distinguished and in what ways internal differences might affect
comparability.

---

REPLY - ??????

There is a lot of duplication and overlap across sections, particularly between
the explanations of DSL types and of asynchronous models, which could be
reduced.

---

REPLY - Disagree

In the problem statement, the illustrations given (Listings 4, 5, and 6) are
helpful but less clearly connected to performance or maintainability
consequences.

---

REPLY - This is related to Problem Statement amd the paper focus and that
external DSL do not support asynchronous calling convetions.

---

The statements in the conclusion, for instance, "synchronous non-blocking
execution using virtual threads consistently delivers performance comparable to
asynchronous," are not strongly supported by the benchmarking outcome,
especially for more complex models like the Stock class.

REPLY - The results showed that for the Stock class, the performance of JStachio using virtual threads
degraded significantly relatively to the Presentations benchmark, however we mention in the text that HtmlFlow using virtual threads did not degrade performance and conclude that
the performance drop could be attributed to the template engine itself, not virtual threads.
Even with the drop, JStachio with virtual threads still performed significantly better than the blocking approach, and was close in performance to the reactive Thymeleaf approach which is also
asynchronous.

The approach shortcomings are discussed very little. For instance, real-world
I/O workloads are more varied and do not scale similarly as synthetic workloads
used here.

REPLY - We acknowledge that synthetic workloads are a limitation of our study. However, when measuring scalability, real-world I/O workloads would introduce randomness related to the network/bandwith that would add noise to the results and make them harder to interpret. By using the 1 millisecond interleaving, we simulate a real-world I/O workload that is consistent and predictable, allowing us to measure the performance of the different approaches in a controlled manner. Real-world workloads could be used to analyze latency and metrics related to memory and CPU utilization, but would compromise the accuracy of throughput and scalability measurements.

The account of Virtual Threads is a good one, but could more clearly compare to
coroutine-based models and specify where one might be preferable to the other.

REPLY - Done with new section in results discussing memory and CPU utilization of
virtual threads vs suspendable coroutines.

---

ANSWER

- REPLY: Already mentioned in Conclusions "without requiring any changes to the
  calling code or exposing its internal complexities". But we will additionally
  highlight thta in related work at the end of the paragraph that introduces
  virtual threads.
- PAPER - TODO - In this paragraph "Only Java virtual threads, introduced in JEP
  4444,..." highlght the advantage of the simplicity of the prgramming model that
  does not rquire expertize in concuurent programming.... bla ...

---

There is some scope to tighten up the structure, perhaps by combining
duplicating subsections and showing benchmark methodology/results more
systematically.

---

- REPLY: Which sections are duplicating? What does he mean by "showing benchmark methodology/results more systematically"?

---

Comments on the Quality of English Language:

The writing is concise, but wordiness, repetitive sentence structure, and
inconsistent technical terminology. Phrasing such as “this work explores how”
and “as a result" appears far too often. Technical accuracy can be enhanced with
more precise language. A professional language editing service is recommended
before resubmission.

---

ANSWER

- PAPER and REPLY: REMOVED repetition.

- Done (?): Removed "this work explores how" and "as a result" from the text. Also reduced the use of "However" in the text.
