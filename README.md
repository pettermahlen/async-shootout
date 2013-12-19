async-test
==========

The purpose of this project is to collect feedback about three different ways to do nested
asynchronous calls in Java:

1. Raw ```ListenableFuture```s
2. RxJava
3. An experimental internal library called Trickle.

If you want to provide some feedback, then follow the following steps:

1. Implement the example graph in each of the three classes: 
[ListenableFutures](src/main/java/com/spotify/asynctest/ListenableFutureThing.java), 
[RxJava](src/main/java/com/spotify/asynctest/RxJavaThing.java), 
[Trickle](src/main/java/com/spotify/asynctest/TrickleThing.java).
2. Head to XXX and let us know what you think.

The example graph is this:

```

       lookup
      /      \
     /        \
    /          \
   /            \
 decorate        log
 using A or B    /
      \        /  (wait for log to complete)
       return 
       decorated result
```
