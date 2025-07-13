Modern web applications depend on **progressive server-side rendering (PSSR)**
where the server streams the webpage content sequentially to the client without
waiting for the whole webpage to be rendered on the server.  
This paper evaluates the effectiveness of using **Java virtual threads**,
introduced in Java 21, for progressive server-side rendering.  
The authors evaluate multiple web template engines using reactive, suspendable,
and virtual thread-based approaches.  
Results from the evaluation show that **Java virtual threads can provide
performance comparable to existing similar approaches under high concurrent
operations**.

I like the idea of the paper.  
The paper effectively establishes the limitations of traditional blocking
template engines (reliance on `Iterable` , lack of async support) in achieving
efficient PSSR within modern low-thread (event-loop) server architectures.  
The **gaps between legacy engines and non-blocking requirements** are
well-defined.  
The authors summarize:

- web templates (DSLs, data models, async support, safety, progressive
  rendering), and
- web framework architectures (thread-per-request vs. event-loop, Spring MVC,
  WebFlux, Quarkus).

This context is crucial for understanding the problem and solution space.  
The table comparing template engines (**Table 1**) is particularly informative.  
The authors consider major Java frameworks (Spring WebFlux, Spring MVC, Quarkus)
in their evaluation to demonstrate the effectiveness of Java virtual threads.  
The inclusion of the **replication package** is also a plus point. However, I
suggest uploading the replication package to an open repository (e.g.,
[zenodo.org](https://zenodo.org)).

---

### Major Concerns

My biggest complaint about the paper is that the **presentation of the results
is difficult to understand/follow**.  
Figures should be **self-explanatory**, but the current presentation is **not up
to the mark**.  
I suggest that the authors look into this issue further.

---

ANSWER:

- TODO: Improve Caption somethink like: "Comparing throghput (i.e. number of
  requestes per second)" among different PSSSR strategies for the PResentation
  data model in Spring....""
- TODO : Axis: include label Nr of Threads or Concurrency.

---

- Done: Added label "Concurrent Users" to the legend of the graphs. Changed captions
  to be more descriptive, e.g., "Throughput (requests per second) scalability results for...

---

The **discussion on virtual thread limitations/overhead** is limited.  
While the results show virtual threads perform well, a brief discussion on
**potential overhead compared to pure reactive approaches (especially under
extreme load)** would add depth.  
Are there scenarios where reactive approaches might still hold an advantage
beyond just template engine efficiency?

---

ANSWER:

- TODO: Related work when introduce virtual threads, analysis on top of the
  results of "Assessing the Efficiency of Java Virtual Threads in Database-Driven
  Server Applications"

---

- Done: new section in results discussing memory and CPU utilization of
- virtual threads vs suspendable coroutines.
- "Assessing the Efficiency of Java Virtual Threads in Database-Driven Server Applications" just shows latency differences when using virtual threads vs
  os threads in the context of web applications with real databases, no throughput/
  memory or CPU utilization analysis.

---

The authors describe the results without **discussing the causes of performance
drops**.  
For example, the paper mentions that **virtual threads experience a more
pronounced decrease in performance** relative to the reactive and suspending
approaches (see **Figure 5**).  
What are the reasons?

---

ANSWER both article and paper.

- Replay: The drop is particularly for JStachio. And we justify that the cause
  is in the template engine itself and not in virtual threads.
- PAPER:... try more clear

---

- Done: clarified that the drop is specific to JStachio and not a general
- issue with virtual threads (gave HtmlFlow as an example of a virtual thread
- approach that did not experience a drop).

---

The paper mentions **reducing the buffer size in Quarkus to 512 bytes** to
enable effective PSSR.  
While this is noted, the **practical implications** of this change should be
briefly discussed.  
Is this a recommended best practice, or are there trade-offs?

---

ANSWER: The same as reviewer 1 and include a percetage value of difference.

---

- Done: Same as review 1
-

---

The authors identify that **Spring MVC doesn't support PSSR effectively** with
the tested engines due to buffering, even with `StreamingResponseBody` and
virtual threads.  
This is a **valuable observation**.  
Could the authors briefly explain why Spring MVC wasn't modified/configured
(e.g., by reducing buffer size similar to Quarkus)?  
Is it a **framework limitation**?

---

ANSWER both:

- REPLY: yes a framework limitation
- PAPER: say that in the paper.

---

- Done: clarified in benchmark section.

---

### Typos and Style Issues

Please carefully check the paper. Here are some examples:

- **Page 1**: `"Attri- bution"` → **"Attribution"** (Context: "Creative Commons
  Attribution license") - Error in the template, fixed

---

ANSWER:

- Fixed hyphenation issue in MDPI template.

---

- **Page 4 (Line 104)**: Missing comma: `"On the other hand an internal DSL"` →
  **"On the other hand, an internal DSL"** - fixed

---

ANSWER:

- Added missing comma: `"On the other hand, an internal DSL"`

---

- **Page 4**: Inconsistent terminology: `"render-blocking resources"` vs.
  `"rendering-blocking resources"` → Use **"render-blocking"** consistently - fixed

---

ANSWER:

- Made terminology consistent throughout paper - used `"render-blocking resources"` consistently instead of mixing with `"rendering-blocking resources"`

---

- **Page 5**: `"they do not support PSSR"` → **"it does not support PSSR"** - fixed

---

ANSWER:

- Fixed subject-verb agreement: `"it does not support PSSR"` instead of `"they do not support PSSR"`

---

- **Page 7**: Missing comma: `"To achieve this KotlinX.html provides"` → **"To
  achieve this, KotlinX.html provides"** - fixed

---

ANSWER:

- Added missing comma: `"To achieve this, KotlinX.html provides"`

---

- **Page 9**: Inconsistent capitalization: `"Vert.X"` → **"Vert.x"** - fixed

---

ANSWER:

- Fixed capitalization to be consistent: `"Vert.x"` instead of `"Vert.X"`

---

- **Page 12**: Repetition: `"However, like other streaming mechanisms...
However, Quarkus allows..."` → Remove first **"However"** - fixed

---

ANSWER:

- Removed repetitive "However" - restructured sentences to eliminate the redundant usage: `"Like other streaming mechanisms... However, Quarkus allows..."`

---

- **Page 14**: `"four main approaches to PSSR"` → I found **three** approaches
  (please check the line) - fixed, blocking approach was missing

---

ANSWER:

- Added the missing blocking approach to make it four approaches to PSSR as stated

---

- **Page 14**: `"According to the Spring documentation"` → Please **include a
  link** to the documentation - fixed

---

ANSWER:

- Added hyperlink reference to Spring documentation using `\url{}` command for proper citation

---

- **Page 15**: `"5.1. Presentations Results"` → **"Evaluation Results"**

---

ANSWER:

- Changed section title to be more descriptive: `"Scalability results for the ... class"` instead of generic `"Presentations Results"`

---

- **Page 19**:
  - `"Publish and Subscriber interfaces"` → Do you mean **"publish-subscribe"?**
  - `"Spring Webflux showed itself the most effective"` → Do you mean: **"Spring
    WebFlux performed the best"?**

---

ANSWER:

- Clarified `"Publisher and Subscriber interfaces"` to be more specific about publish-subscribe pattern
- "Most effective" is correct and refers to enabling PSSR itself, not performance. Spring WebFlux uses reactive streams (Flux/Mono) 
to immediately send data chunks as they become available, as opposed to Quarkus and Spring MVC which flush the response buffer when it
reaches capacity instead. In that sense, the WebFlux is the most effective at enabling PSSR.

---

### References

Please consider including relevant papers related to **Java virtual threads**.
Suggested references:

1. _Java Single vs. Platform vs. Virtual Threads Runtime Performance Assessment
   in the Context of Key Class Detection_, SACI, 2024
2. _Considerations for Integrating Virtual Threads in a Java Framework: A
   Quarkus Example in a Resource-Constrained Environment_, DEBS, 2023
3. _Assessing the Efficiency of Java Virtual Threads in Database-Driven Server
   Applications_, MIPRO, 2024
