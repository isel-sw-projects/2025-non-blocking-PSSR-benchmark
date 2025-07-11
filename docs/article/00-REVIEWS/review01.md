The paper addresses the area of web development, with the increasing adoption of
Java Virtual Threads.

1. It would be better to elaborate the hardware specifications including the
   **exact CPU model** that influences instruction set support and performance
   characteristics, **clock speed** that boosts frequencies of the CPU, **disk
   I/O characteristics**, **network bandwidth/latency to test client**, **JVM
   arguments**, **garbage collector used**, **platform thread pool size**, etc.
***
ANSWER:
* **exact CPU model** ???
* **clock speed** *** ??? check to print from shell script ???
* **disk I/O characteristics**,
* **network bandwidth/latency to test client**, 
* **JVM arguments**, - YES
* **garbage collector used**,  - YES
* **platform thread pool size**,  - YES
***
2. Measure and compare **TTFB** across approaches to justify the claim of
   *"improving perceived load times."*  
***
ANSWER:
* TO DO: In 2ns paragraph of Intro include statement about TTTDB in PSSR and 
refer teh results presented in Carvalho WISE paper.
***
3. Provide statistical justification for *"JMeter and Apache Bench show no
   significant differences"* (line 630).
***
ANSWER: 
* TODO: include a number that quantifies the difference.
***
ANSWER: 
* TODO: include.
4. How many independent test runs were conducted for each configuration?  
***
5. Include **error bars** (e.g., 95% confidence intervals) on the bar charts to
   assess the significance of differences.
***
ANSWER:
- EXCLUDE - Confidence is bigger than 99% and the error bar os not visible in chart.
***
6. Provide data on **CPU utilization**, **memory consumption**, and **GC
   activity** for each approach and describe *why* certain approaches perform
   better or worse.  
ANSWER:
* TO DO: Try some sort of GC anaylisis maybe run the server with: java -Xrunhprof:cpu=samples,file=myprogram.hprof
  And then run Jmeter or ab and collect the GC activity.
***
7. Discuss: do **virtual threads genuinely reduce memory overhead** for
   concurrent connections compared to traditional threads?
***
ANSWER: ADD some reference e.g. "Comparison of Structured Concurrency Constructs in Java and Kotlin – Virtual Threads and Coroutines"
***
8. Currently, you have used simple measures of complexity, with the stock class
   having *"approximately two times as many data bindings."* Real-world
   templates can have deeply nested data structures, complex conditional logic,
   and iterative rendering over thousands of items. **How would the performance
   characteristics change under such conditions?**
9. Discuss the **limitations of the chosen data models** for generalizability.  
***
ANSWER: 
* TODO - Incluir uma especulação. 
***
10. The paper doesn't discuss the *actual client-side rendering behavior*. Do
    certain PSSR strategies lead to **CLS** in the browser? How do different
    browsers handle the progressively streamed HTML? Discuss **bridging the
    gap** between server-side performance and actual user experience.
ANSWER: 
* Exclude - reply in comments.
***
11. Consider lines 626–629. You have configured both Quarkus and Spring MVC with
    an **8KB buffer for consistency**, despite Quarkus not enabling PSSR at this
    size. However, later (lines 601–604), it states that **Quarkus was reduced
    to 512 bytes** for PSSR. Improve the clarity of the description by
    presenting **two sets of results for Quarkus**:  
    - one at **8KB** (for direct comparison to Spring MVC's default behavior),
      and  
    - another at **512 bytes** (to demonstrate its full PSSR capability).
***
ANSWER:
* TO DO: Specifu in the article the performance downgrade in percentage in Quarkus
when the buffer is 512 Kb.

