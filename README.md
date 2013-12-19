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
 decorate        log
 using A or B    /
      \        /  (wait for log to complete)
       return 
       decorated result
```

That is, first do a lookup of something using two input parameters, then check the results of the lookup and 
do either decoration A or decoration B. Also make sure to log the results of the lookup, and once the log is complete
and the decorated result is available, return it. There is a
[synchronous reference implementation](src/main/java/com/spotify/asynctest/TrickleThing.java) that should provide
some more information, and there are unit tests that should only work when you're done.

