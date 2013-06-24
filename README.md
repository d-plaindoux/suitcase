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

  isZero.caseOf(0).then.constant(true);
  isZero.caseOf(_).then.constant(false);
    
  isZero.match(0); // == true
</pre>

Matching and Typing
-------------------

This pattern matching also offers a simple mechanism able to discriminate objects using their types.

For instance the following sample checks if an object is an integer or a string.

<pre>
  final Matcher&lt;Object, String> typeCase = Matcher.create();

  typeCase.caseOf(Integer.class).then.constant("int");
  typeCase.caseOf(String.class).then.constant("string");

  typeCase.match(0);       // == "int"
  typeCase.match("Hello"); // == "string"
</pre>

Matching using Regular Expressions
----------------------------------

Indeed a string can be matched using inherited equality predicate but the selection can also be performed using
more complex structure like regular expressions. In the next example each sentence starting with <tt>Hello,</tt>
and ending with <tt>!</tt> are matched returning <tt>true</tt>. Any other sentences are not matched and then the
result is <tt>false</tt>.

<pre>
  final Matcher&lt;String, Boolean> typeCase = Matcher.create();

  typeCase.caseOf(Regex("Hello,.*!")).then.constant(true);
  typeCase.caseOf(_).then.constant(false);

  typeCase.match("Hello, World!);   // == true
  typeCase.match("Bob, Hello");     // == false
</pre>

Matching complex Objects 
------------------------

The pattern system is open and accept ad-hoc cases. For instance for the `Lists` two patterns are available:
* `Empty()` for the empty list and
* `Cons(?,?)` for lists containing at least one element.

Then a simple function able to check when a list is empty can be proposed.

<pre>
  final Matcher&lt;List&lt;Object>, Boolean> isEmpty = Matcher.create();

  isEmpty.caseOf(Empty()).then.constant(true);
  isEmpty.caseOf(_).then.constant(false);

  isEmpty.match(Arrays.&lt;Object>asList());            // == true
  isEmpty.match(Arrays.&lt;Object>asList(1));           // == false
</pre>

It's also possible to capture list elements like the head and the tail. For instance we can propose a matcher
able to add all integers in a given list.

<pre>
  final Matcher&lt;List&lt;Integer>, Boolean> addAll = Matcher.create();

  addAll.caseOf(Empty()).then.constant(0);
  addAll.caseOf(Cons(var,var)).then.function(
        new Function2&lt;Integer, List&lt;Integer>, Integer>() {
            public Integer apply(Integer i, List&lt;Integer> l) {
                return i + allAdd.match(l);
            }
        });

  addAll.match(Arrays.&lt;Integer>asList());          // == 0
  addAll.match(Arrays.&lt;Integer>asList(1,2,3,4,5)); // == 15
</pre>

Such matcher using Java 8 can be expressed differently using lambda expressions.

<pre>
  final Matcher&lt;List&lt;Integer>, Boolean> addAll = Matcher.create();

  addAll.caseOf(Empty()).then.constant(0);
  addAll.caseOf(Cons(var,var)).then.function((Integer i, List&lt;Integer> l) -> i + allAdd.match(l));

  addAll.match(Arrays.&lt;Integer>asList());          // == 0
  addAll.match(Arrays.&lt;Integer>asList(1,2,3,4,5)); // == 15
</pre>

Of course such approach is not efficient for one reason: a stack overflow can occurs if the list contains
to many integers.

Other Propositions
------------------

Some propositions are also available for this purpose like:
* [Towards Pattern Matching in Java](http://kerflyn.wordpress.com/2012/05/09/towards-pattern-matching-in-java/)