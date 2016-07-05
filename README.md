# SuitCase 

[![Build Status](https://travis-ci.org/d-plaindoux/suitcase.svg?branch=master)](https://travis-ci.org/d-plaindoux/suitcase) 
[![Coverage Status](https://coveralls.io/repos/d-plaindoux/suitcase/badge.png)](https://coveralls.io/r/d-plaindoux/suitcase)
[![stable](http://badges.github.io/stability-badges/dist/stable.svg)](http://github.com/badges/stability-badges)
[![Maven Central](https://img.shields.io/maven-central/v/org.smallibs/suitcase.svg)][search.maven]

SuitCase is a convenient Java library dedicated to object manipulation using `pattern matching` mechanism.

## Quick Overview

If you want to manipulate object based on types in Java the dedicated design pattern is the visitor.
Unfortunately even if such pattern is well known its implementation is always painful because it implies
code dissemination and finally brakes incremental compilation approach.

In addition such mechanism only enables selection based on types and does not provides a simple and
intuitive mechanism filtering objects using their values i.e. attributes.

For this purpose a simple pattern matching inspired by Scala 
[extractor object](http://docs.scala-lang.org/tutorials/tour/extractor-objects.html) has been designed.

This pattern matching offers a simple mechanism for simple object selection based on intrinsic equality.
Indeed pattern matching cases can be done on the object kind and it's internal state. For instance the following 
sample checks if an integer is <tt>O</tt> or not.

``` java
Match<Integer, Boolean> isZero = Match.match();

isZero.caseOf(0).then(true);
isZero.caseOf(Any()).then(false);

isZero.match(0); // == true
```

## Regexp, List Patterns and more ...

More information and descriptions are given in the [Wiki](https://github.com/d-plaindoux/suitcase/wiki) and in 
particular ad-hoc case is explained with practical implementations covering matching for lists, string regular 
expression, XML etc.

## Releases

This library is available at Sonatype OSS Repository Hosting service and can be simply used adding the following 
dependency to your pom project.

```
<dependency>
  <groupId>org.smallibs</groupId>
  <artifactId>suitcase</artifactId>
  <version>0.3</version>
</dependency>
```

## Presentation

A [presentation (in french)](http://www.slideshare.net/dplaindoux/java-amp-le-pattern-matching-54806648) has been given 
during a [Toulouse Java User Group](http://toulousejug.org) session.

## Other Propositions & Discussions
   
A review of various approaches is given in the [Structural Pattern Matching in Java](http://blog.higher-order.com/blog/2009/08/21/structural-pattern-matching-in-java/). 
It starts from the visitor and proposes alternatives with a functional programming approach.

Some propositions like [Towards Pattern Matching in Java](http://kerflyn.wordpress.com/2012/05/09/towards-pattern-matching-in-java/)
or [Java 8 annotation processor and framework](https://github.com/derive4j/derive4j) for deriving algebraic data types constructors, 
pattern-matching, morphisms are also available.

## License

Copyright (C)2015 D. Plaindoux.

This program is  free software; you can redistribute  it and/or modify
it  under the  terms  of  the GNU  Lesser  General  Public License  as
published by  the Free Software  Foundation; either version 2,  or (at
your option) any later version.

This program  is distributed in the  hope that it will  be useful, but
WITHOUT   ANY  WARRANTY;   without  even   the  implied   warranty  of
MERCHANTABILITY  or FITNESS  FOR  A PARTICULAR  PURPOSE.  See the  GNU
Lesser General Public License for more details.

You  should have  received a  copy of  the GNU  Lesser General  Public
License along with  this program; see the file COPYING.  If not, write
to the  Free Software Foundation,  675 Mass Ave, Cambridge,  MA 02139,
USA.
