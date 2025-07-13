The paper addresses the area of web development, with the increasing adoption of
Java Virtual Threads.

1. It would be better to elaborate the hardware specifications including the
   **exact CPU model** that influences instruction set support and performance
   characteristics, **clock speed** that boosts frequencies of the CPU, **disk
   I/O characteristics**, **network bandwidth/latency to test client**, **JVM
   arguments**, **garbage collector used**, **platform thread pool size**, etc.

---

ANSWER:

- **exact CPU model** ???
- **clock speed** \*\*\* ??? check to print from shell script ???
- **disk I/O characteristics**,
- **network bandwidth/latency to test client**,
- **JVM arguments**, - YES
- **garbage collector used**, - YES
- **platform thread pool size**, - YES

---

- DONE: Added in the Results section first paragraph:
- **exact CPU model**: AMD EPYC 7763 64-Core Processor (x86_64 architecture)
- **clock speed**: 3.24 GHz operating frequency
- **disk I/O characteristics**: 75GB SSD with ext4 filesystem, achieving 1.5 GB/s write throughput
- **network bandwidth/latency to test client**: 1500 MTU Ethernet interface
- **JVM arguments**: OpenJDK 21 (Temurin build) with max heap size of 16GB
- **garbage collector used**: G1 garbage collector enabled by default
- **platform thread pool size**: Unlimited for both Spring MVC and Quarkus

---

1. Measure and compare **TTFB** across approaches to justify the claim of
   _"improving perceived load times."_

---

ANSWER:

- TO DO: In 2ns paragraph of Intro include statement about TTTDB in PSSR and
  refer teh results presented in Carvalho WISE paper.

---

- DONE: Added in introduction paragraph 2, reference and statement about TTFB.

---

1. Provide statistical justification for _"JMeter and Apache Bench show no
   significant differences"_ (line 630).

---

ANSWER:

- TODO: include a number that quantifies the difference.

---

ANSWER:

- TODO: include.

1. How many independent test runs were conducted for each configuration?

---

- DONE: Added in the Results section first paragraph:
- **number of independent test runs**: 5 runs per configuration to ensure reliability of results.

---

1. Include **error bars** (e.g., 95% confidence intervals) on the bar charts to
   assess the significance of differences.

---

ANSWER:

- EXCLUDE - Confidence is bigger than 99% and the error bar os not visible in chart.

---

1. Provide data on **CPU utilization**, **memory consumption**, and **GC
   activity** for each approach and describe _why_ certain approaches perform
   better or worse.  
   ANSWER:

- TO DO: Try some sort of GC anaylisis maybe run the server with: java -Xrunhprof:cpu=samples,file=myprogram.hprof
  And then run Jmeter or ab and collect the GC activity.

---

- DONE: Added a results section for memory and cpu utilization, analysis.
- TO DO: Check if the contents seem relevant and if the analysis is correct.

1. Discuss: do **virtual threads genuinely reduce memory overhead** for
   concurrent connections compared to traditional threads?

---

ANSWER: ADD some reference e.g. "Comparison of Structured Concurrency Constructs in Java and Kotlin – Virtual Threads and Coroutines"

---

1. Currently, you have used simple measures of complexity, with the stock class
   having _"approximately two times as many data bindings."_ Real-world
   templates can have deeply nested data structures, complex conditional logic,
   and iterative rendering over thousands of items. **How would the performance
   characteristics change under such conditions?**
2. Discuss the **limitations of the chosen data models** for generalizability.

---

ANSWER:

- TODO - Incluir uma especulação.

---

- DONE: Added in the Results section last paragraph.

---

1.  The paper doesn't discuss the _actual client-side rendering behavior_. Do
    certain PSSR strategies lead to **CLS** in the browser? How do different
    browsers handle the progressively streamed HTML? Discuss **bridging the
    gap** between server-side performance and actual user experience.
    ANSWER:

- Exclude - reply in comments.

---

1.  Consider lines 626–629. You have configured both Quarkus and Spring MVC with
    an **8KB buffer for consistency**, despite Quarkus not enabling PSSR at this
    size. However, later (lines 601–604), it states that **Quarkus was reduced
    to 512 bytes** for PSSR. Improve the clarity of the description by
    presenting **two sets of results for Quarkus**:
    - one at **8KB** (for direct comparison to Spring MVC's default behavior),
      and
    - another at **512 bytes** (to demonstrate its full PSSR capability).

---

ANSWER:

- TO DO: Specifu in the article the performance downgrade in percentage in Quarkus
  when the buffer is 512 Kb.

---

- DONE: Added in the Results section 0.046% difference in performance
  when the buffer is 512 Kb.
