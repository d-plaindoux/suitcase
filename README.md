# SuitCase 

[![Build Status](https://travis-ci.org/d-plaindoux/suitcase.svg?branch=master)](https://travis-ci.org/d-plaindoux/suitcase) [![Coverage Status](https://coveralls.io/repos/d-plaindoux/suitcase/badge.png)](https://coveralls.io/r/d-plaindoux/suitcase)

SuitCase is a convenient Java library dedicated to object manipulation using `pattern matching` mechanism.

## Quick Overview

If you want to manipulate object based on types in Java the dedicated design pattern is the visitor.
Unfortunately even if such pattern is well known its implementation is always painful because it implies
code dissemination and finally brakes incremental compilation approach.

In addition such mechanism only enables selection based on types and does not provides a simple and
intuitive mechanism filtering objects using their values i.e. attributes.

For this purpose a simple pattern matching inspired by Scala [extractor object](http://www.scala-lang.org/node/112)
has been designed.

This pattern matching offers a simple mechanism for simple object selection based on intrinsic equality.
Indeed pattern matching cases can be done on the object kind and it's internal state. For instance the following sample
checks if an integer is <tt>O</tt> or not.

``` java
final Match<Integer, Boolean> isZero = Match.match();

isZero.caseOf(0).then(true);
isZero.caseOf(__).then(false);

isZero.match(0); // == true
```

## Regexp, XML, List Patterns and more ...

More information and descriptions are given in the [Wiki](https://github.com/d-plaindoux/suitcase/wiki) and in particular ad-hoc case is explained with
practical implementations covering matching for lists, string regular expression, XML etc.

See also:

* [Core Pattern Matching](https://github.com/d-plaindoux/suitcase/wiki#core-pattern-matching)
* [Generic Parser](https://github.com/d-plaindoux/suitcase/wiki#generic-parser)

## Other Propositions

Some propositions are also available for this purpose like:
* [Towards Pattern Matching in Java](http://kerflyn.wordpress.com/2012/05/09/towards-pattern-matching-in-java/)

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
