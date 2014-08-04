## Barista

Barista compiles Java source code to Javascript via CoffeeScript (phew).
It current gets as far as CoffeeScript and there is a lot left to do.
Some stuff it does already are a lot of statements, expressions, vars, classes fields, some static references class definitions.  Sounds like a lot, but there is a world of minor edge cases I'm sure a serious user will uncover.
Thankfully antlr makes this relatively easy to find and deal with, just have to do the work.

As a learning expression, fun stuff has been rewriting Javas for expressions in terms of CoffeeScripts while and switch statements in terms of CoffeeScript if elses since CS's does not offer fall through - yes I wanted to keep that feature :)

## What, why CoffeeScript?

Reusing the class mechanism is a nice thing to have, plus CoffeeScript is syntactically a very easy target.
Personally I prefer CoffeeScript to Javascript as a language so stopping at the intermediate step is also attractice.

## What is the goal?

Preserve as much semantically as possible from the original Java program and translate it to Javascript.
In spirit it is similar to Strongly-Typed Javascript.  That project had a few kinks that made wonder what I could
better if anything at all.  I remember not liking its wrapper integration design and I think that has a huge
impact on dev code since js lib reuse is a necessary goal of any java->js project.

## Why Java?

Tooling tooling tooling is one big one.  As a primary Java/Javascript, I'm very familiar with the warts of both languages.
I love my js flexibility when starting out on projects but inevitably I need my Java tooling when it gets large.
Using Java as an interface to Javascript creates rigidity through its class-based lens but I think it will help.
I also see secondary benefits such as an established dependency tool like maven.

In a perfect world all Barista projects would come with some sort of easy publish to webjar buttons.  If you haven't checked out webjars yet, please do!  ([webjars.org](http://webjars.org/))  

## Java 8?

Nope.  I'm no grammar writer.  If I ever finish this project in some form and some enterprising fellow puts up a Java 8 grammar, maybe.

## How much have you done?

Not enough.  The proof of concept is there, but I still need wrapping types definition format, 
some sort of interpreter to resolve static vs member fields etc, a simple library for early dom objects,
Test cases, documentation etc etc etc. 

A lot of the work so far has been work on actually translating syntax correctly, respecting indent levels etc.
I do this in a very manual parse tree walk.  You wont find an optimizing multipass compiler here, this is one high level language to another - simple translating.  I suspect this is probably more than adequate for the 85% rule so I still
think this is a worthy project to complete in some form.

## Plans

I will try to get back to it eventually, right now its hard to want to play with Java 
(even though antlr and antlr4ide make it honestly dreamy) while I'm in the midst of banging out clojure.