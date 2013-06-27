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

Matching and Equality
---------------------

This pattern matching offers a simple mechanism for simple object selection based on intrinsic equality.
This is available for basic values like boolean, integers etc. and POJOs.

For instance the following sample checks if an integer is <tt>O</tt> or not.

<pre>
  final Match&lt;Integer, Boolean> isZero = Match.match();

  isZero.caseOf(0).then.value(true);
  isZero.caseOf(_).then.value(false);
    
  isZero.match(0); // == true
</pre>

Matching complex Objects 
------------------------

The pattern system is open and accept ad-hoc cases. For instance for the `Lists` two patterns are available:
* `Empty()` for the empty list and
* `Cons(?,?)` for lists containing at least one element.

Then a simple function able to check when a list is empty can be proposed.

<pre>
  final Matcher&lt;List&lt;Object>, Boolean> isEmpty = Matcher.create();

  isEmpty.caseOf(Empty()).then.value(true);
  isEmpty.caseOf(_).then.value(false);

  isEmpty.match(Arrays.&lt;Object>asList());            // == true
  isEmpty.match(Arrays.&lt;Object>asList(1));           // == false
</pre>

Ad-Hoc Case class and more ...
------------------------------

More information and descriptions are given in the [corresponding Wiki](https://github.com/d-plaindoux/suitcase/wiki/Introduction)

Other Propositions
------------------

Some propositions are also available for this purpose like:
* [Towards Pattern Matching in Java](http://kerflyn.wordpress.com/2012/05/09/towards-pattern-matching-in-java/)