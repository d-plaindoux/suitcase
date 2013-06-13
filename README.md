SuitCase
========

SuitCase is a convenient Java library dedicated to object manipulation using `pattern matching` mechanism.

Overview
--------

If you want to manipulate object based on types in Java the dedicated design pattern is the visitor.
Unfortunately even if such pattern is well known its implementation is always painful because it implies
code dissemination and finally brakes incremental compilation approach.

In addition such mechanism only enables selection based on types and does not provides a simple and
intuitive mechanism filtering objects using their values i.e. attributes.

For this purpose a simple pattern matching inspired by Scala [extractor object](http://www.scala-lang.org/node/112)
has been designed.

Matching by equality
--------------------

This pattern matching offers a simple mechanism for simple object selection based on intrinsic equality.
This is available for basic values like boolean, integers etc. and POJOs.

For instance the following sample checks if an integer is <tt>O</tt> or not.

<pre>
  final Match&lt;Integer, Boolean> isZero = Match.&lt;Integer, Boolean>match().
    when(0).then(true).
    when(_).then(false);
    
  // isZero.apply(0) => true 
</pre>

Matching complex Objects 
------------------------

The pattern system is open and accept ad-hoc cases. For instance for the `List` two patterns has been proposed:
* `Nil` for empty list and
* `Cons` for the other (contains one element or more).

Then a simple function able to check when a list is empty can be proposed.

<pre>
  final Match&lt;List, Boolean> isEmpty = Match.&lt;List, Boolean>match().
    when(new Nil()).then(true).
    when(new Cons(_,_)).then(false);

    // isEmpty.apply(Arrays.asList())   => true
    // isEmpty.apply(Arrays.asList(1))  => false
</pre>

It's also possible to capture list elements like the head and the tail. For instance we can propose a `function`
able to add all integers in a given list.

<pre>
  final Match&lt;List&lt;Integer>, Boolean> addAll = Match.match();

  addAll.
    when(new Nil()).then(0).
    when(new Cons(_,_)).then(
        new Function&lt;Couple&lt;Integer, List>, Integer>() {
            @Override
            public Integer apply(Couple&lt;Integer, List> couple) throws MatchingException {
                return couple._1 + allAdd.apply(couple._2);
            }
        });

    // addAll.apply(Arrays.&lt;Integer>asList())  => 0
    // addAll.apply(Arrays.asList(1,2,3,4,5))  => 15
</pre>

Of course such approach is not efficient for one reason: a stack overflow can occurs if the list contains
to many integers.


Other Propositions
------------------

Some propositions are also available for this purpose like:
* [Towards Pattern Matching in Java](http://kerflyn.wordpress.com/2012/05/09/towards-pattern-matching-in-java/)