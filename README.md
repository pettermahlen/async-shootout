async-test
==========

The purpose of this project is to collect feedback about three different ways to do nested
asynchronous calls in Java:

1. Raw ListenableFutures - from Guava.
2. RxJava - a library produced by Netflix, based on work done by Erik Meijer in .NET.
3. An experimental internal library called Trickle.

If you want to provide some feedback, then follow the following steps:

1. Implement an example call graph in each of the three classes: 
[ListenableFutures](src/main/java/com/spotify/asynctest/ListenableFutureThing.java), 
[RxJava](src/main/java/com/spotify/asynctest/RxJavaThing.java) and
[Trickle](src/main/java/com/spotify/asynctest/TrickleThing.java).
2. Head to https://docs.google.com/a/spotify.com/forms/d/1urZQ0DWQxgX2ky-Sq3_zpLHY-avKGo5sfxJE6JAo17g/viewform
and let us know what you thought of the different frameworks.

The example graph is this:

```

         lookup
        /      \
       /        \
 decorate        log
 using A or B    /
       \       /  (wait for log to complete)
         return 
    decorated result
```

That is, first do a lookup of something using two input parameters, then check the results of the lookup and 
do either decoration A or decoration B. If the decoration fails, you should return a default value.
Also make sure to log the results of the lookup, and once the log is complete
and the decorated result is available, return it. There is a
[synchronous reference implementation](src/main/java/com/spotify/asynctest/SynchronousThing.java) that should provide
some more information, and there are unit tests that should only work when you're done.

NOTE FOR CHEATERS:
The commit history of this repo has implementations using the three frameworks. If you get stuck (and want to cheat), 
you can search through the commit log and find something that works - but is not necessarily great. It's better for 
us if you do all the work on your own, of course.
