SuitCase
========

SuitCase is a convenient library dedicated to object manipulation using `patterm matching` mechanism.

Overview
--------

If you want to manipulate object in Java a well known pattern is the visitor. Unfortunately even if
such pattern is well known its implementation is always painful because it implies code dissimination
and finally brakes incremental compilation approach.

In addition such mechanis is only enable selection based on types and does not provides a simple and 
intuive mechanism filtering objects using their values i.e. attributes.

For this purpose a simple pattern matching is proposed based on Scala [extractor objet](http://www.scala-lang.org/node/112) 

Matching by equality
-------

The pattern matching offers a simple mechanims for simple object selection based on intrinsic equality. 
This is available also for basic values like booleans, integers etc.

The instance the following sample checks is an integer is <tt>O</tt> or not. 

<pre>
  final Match&lt;Integer, Boolean> isZero = Match.&lt;Integer, Boolean>match().
    with(0).then(true).
    with(Cases.&lt;Integer>_()).then(false);
    
  // isZero.apply(0) => true 
</pre>

Matching complex Objects 
-------

The pattern is system is open and accept ad-hoc cases. For instance for the `List` two patterns `Nil` 
and `Cons` are proposed.

Then a simple function able to check when a list is empty can be the following one:

<pre>
  final Match<List, Boolean> isEmpty = Match.<List, Boolean>match().
    with(new Nil()).then(true).
    with(new Cons()).then(false);

    // isEmpty.apply(Arrays.asList())   => true
    // isEmpty.apply(Arrays.asList(1))  => false
</pre>

