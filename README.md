# Lambda
A simple [Lambda Calculus](https://en.wikipedia.org/wiki/Lambda_calculus) expression interpreter in Java

![Screenshot](https://github.com/James-P-D/Lambda/blob/master/Screenshot.gif)

## Contents

1. [Lambda Calculus](#Lambda-Calculus)
    1. [History](#History)
    2. [Beta Reduction](#Beta-Reduction)
    3. [Alpha Equivalence](#Alpha-Equivalence)
2. [The Application](#The-Application)
    1. [Help](#Help)
    3. [Loading Files](#Loading-Files)    
    4. [Debug Mode](#Debug-Mode)
    5. [Alpha Mode](#Alpha-Mode)
    6. [Quitting](#Quitting)
    7. [Library Files](#Library-Files)
    8. [Examples](#Examples)
3. [Building Notes and Problems](#Building-Notes-and-Problems)
4. [Acknowledgements](#Acknowledgements)
    
## Lambda Calculus

If you already understand the Lambda Calculus, you can skip this section and head straight to [The Application](#The-Application).  

```
<expression>  = <name> | <function> | <application>
<function>    = λ<name>.<expression>
<application> = <expression> <expression>
```

### History

### Beta Reduction

### Alpha Equivalence

## The Application

After running the application you will be presented with a short introductory message followed by the prompt into which you can enter lambda expressions or terms:

```
λ-CALCULUS: Type 'help' for more information
λ>
```

We can enter lambda expressions at the `λ>` prompt, which the application will attempt to beta-reduce:

```
λ-CALCULUS: Type 'help' for more information
λ> λx.x
β> λx.x
```

In the above we entered the expression `λx.x` which cannot be reduced so we see the `β>` prompt followed by our initial expression.

We can also create terms and then apply them:

```
λ> id=λx.x
β> id = λx.x
λ> id y
β> y
```

In the above we create a term called `id` and then apply the symbol `y` to it, which gives `y`.

### Help

If you enter `help` you will receive the following information:

```
λ> help
HELP: Help information
HELP: Commands: help            - this screen
HELP:           terms           - display all known terms
HELP:           debug           - toggle debug mode
HELP:           load <filename> - loads file
HELP:           alpha           - alpha-equivalence
```

### Loading Files

It is possible to load files which contain terms or expressions by using the `load` command. For example, the application comes with a [booleans.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/booleans.lbd) file which contains the following:

```
# True and false values
true = \x.\y.x
false = \x.\y.y

# Bunch of normal operators for booleans
and = \a.\b.a b a
or = \a.\b.a a b
not = \a.a false true
xor = \x.\y. x (not y) y
```

In the above file we have used the `\` symbol for `λ` character. Either one is fine, just make that if you are consistent and if using greek letters, that you save the file as UTF-8.

Also note that any line beginning with a `#` symbol will be treated as a comment and ignored.

We can load the file easily enough:

```
λ> load booleans.lbd
LOADING FILE: booleans.lbd
LOADING FILE: booleans.lbd - 6 term(s) and 0 expression(s) parsed
```

Having loaded the file, we can now perform boolean expressions

```
λ> not true
β> false
λ> or false true
β> true
```

Some files may require terms which exist in *other* files. In these cases we can start the file with a `$` symbol followed by the names of other files we need to include.

For example [maths.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/maths.lbd) file contains the following:

```
$ booleans.lbd

zero  = \f.\a.a
one   = \f.\a.f a
two   = \f.\a.f (f a)
three = \f.\a.f (f (f a))
four  = \f.\a.f (f (f (f a)))
five  = \f.\a.f (f (f (f (f a))))
six   = \f.\a.f (f (f (f (f (f a)))))
seven = \f.\a.f (f (f (f (f (f (f a))))))
eight = \f.\a.f (f (f (f (f (f (f (f a)))))))
nine  = \f.\a.f (f (f (f (f (f (f (f (f a))))))))
ten   = \f.\a.f (f (f (f (f (f (f (f (f (f a)))))))))

succ = \n.\f.\a.f (n f a)
add = \m.\n.\f.\a.m f (n f a)
mult = \m.\n.\f.m (n f)
pow = \m.\n.n m
pred = \n.\f.\a.n (\g.\h.h (g f)) (\u.a) (\u.u)
sub = \m.\n.n pred m
is_zero = \n.n (\m.false) true
```

When we attempt to load the file we will see the following:

```
λ> load maths.lbd
LOADING FILE: maths.lbd
LOADING FILE: booleans.lbd
LOADING FILE: booleans.lbd - 6 term(s) and 0 expression(s) parsed
LOADING FILE: maths.lbd - 18 term(s) and 0 expression(s) parsed
```

As soon as we start loading the terms in `maths.lbd`, the application finds the line `$ booleans.lbd` and knows it needs to import the terms from that file otherwise there would be errors when we encounter terms like `true` and `false` which are used in `maths.lbd`.

Finally, it is also possible to load files on startup by passing filenames as parameters to the .class file. For more information on building the project, see the [Building Notes and Problems](#Building-Notes-and-Problems) section.

### Debug Mode

You can enter `debug` to toggle debug-mode:

```
λ> debug
DEBUG: Debug mode ON
λ> debug
DEBUG: Debug mode OFF
```

Debug-mode can be useful for showing the intermediate steps during beta-reduction. Below is the result of beta-reducing `or false true` with debug-mode turned on:

```
λ> debug
DEBUG: Debug mode ON
λ> or false true
β> ((λa.λb.(a(λx.λy.x))b)(λx.λy.y))(λx.λy.x)
β> (λb.((λx.λy.y)(λx.λy.x))b)(λx.λy.x)
β> ((λx.λy.y)(λx.λy.x))(λx.λy.x)
β> (λy.y)(λx.λy.x)
β> λx.λy.x
β> true
```

### Alpha Mode

You can enter `alpha` to switch into alpha-equivalence mode. Simply enter a number of expressions, each on a separate line and then a blank line:

```
λ> alpha
α-EQUIVALENCE: Enter a number of expressions, each on a separate line, and then an empty line to begin comparison
α1> λa.b a
α2> λc.d c
α3> λb.a b
α4>
α> TRUE
```

In the above example we see `TRUE` reported because `λa.b a`, `λc.d c` and `λb.a b` are all equivalent to each other.

However, consider the following example:

```
λ> alpha
α-EQUIVALENCE: Enter a number of expressions, each on a separate line, and then an empty line to begin comparison
α1> λa.b a
α2> λc.d c
α3> λb.a a
α4>
α> FALSE
```

This the above output, we see the result `FALSE`. This is because although `λa.b a` and `λc.d c` are equivalent, `λb.a a` is *not* equivalent to either of them.

### Quitting

Finally, you can terminate the program by entering `quit`, `exit` or pressing the <kbd>ESC</kbd> key.

```
λ> quit
QUITTING: Bye!
```

### Library Files

As already mentioned, the application comes with a number of library files which contain definitions for boolean operators, maths, conditionals, etc:

* [booleans.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/booleans.lbd) - True, false, and, or, etc.
* [maths.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/maths.lbd) - Numbers 1-10, add, subtract, etc.
* [tuples.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/tuples.lbd) - Pairs of values.
* [conditionals.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/conditionals.lbd) - If..then..else, equality, greater-than-or-equal, etc.
* [lists.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/lists.lbd) - Head, tail, etc.
* [functions.lbd](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/functions.lbd) - Recursion, etc.

### Examples

Now that we have a full understanding of Lambda-Calculus and of how the application works, we can start writing some programs.

After loading `booleans.lbd` we can perform `and`, `or` and `not` operations:

```
λ> load booleans.lbd
λ> not true
β> false
λ> and true false
β> false
λ> or true false
β> true
```

After loading `maths.lbd` we can perform basic mathematical operations:

```
λ> load maths.lbd
λ> add one two
β> three
λ> mult two two
β> four
λ> succ one
β> two
λ> succ (succ one)
β> three
```

Note that because `maths.lbd` only contains term definitions for the first ten numbers, mathematical operations which return larger numbers will be displayed in lambda-syntax:

```
λ> load maths.lbd
λ> add nine nine
β> λf.λa.f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f(f a)))))))))))))))))
```

After loading `tuples.lbd` we can create pairs of values:

```
λ> load maths.lbd
λ> load tuples.lbd
λ> first (pair one two)
β> one
λ> second (pair one two)
β> two
```

We can even have pairs of pairs:

```
λ> second (pair one (pair two three))
β> λf.(f(λf.λa.f(f a)))(λf.λa.f(f(f a)))
```

The application is unable to resolve multiple-terms, but if we check what the expected answer (`pair two three`) is in lambda-syntax, we can see that the lambda sequences matches:

```
λ> pair two three
β> λf.(f(λf.λa.f(f a)))(λf.λa.f(f(f a)))
```

After loading `conditionals.lbd` we can check for equality with `if_then_else`:

```
λ> if_then_else true one two
β> one
λ> if_then_else false one two
β> two
```

### Building Notes and Problems

The building instructions can be found [runlambda.bat](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/runlambda.bat) and are also included below:

```
UPDATE HERE
UPDATE HERE
UPDATE HERE
UPDATE HERE
UPDATE HERE
UPDATE HERE
```

A fair amount of work went into getting the console I/O to work and display nicely. Since Java is fussy about supporting single-keypress-input, displaying Unicode characters (`λ`, `α`, `β` etc.) and using colours, we need to use a bunch of Windows-specific stuff to achieve this. If you are struggling with inputting strings, or are seeing strange, non-printable values in the console, simply set `FANCY_UI` to `false` in [Console.Java](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/Console.java).

### Acknowledgements

The application uses [RawConsoleInput](https://www.source-code.biz/snippets/java/RawConsoleInput/) courtesy of [Christian d'Heureuse](https://stackoverflow.com/questions/1066318/how-to-read-a-single-char-from-the-console-in-java-as-the-user-types-it/30008252#30008252) for the single-character-input, and [Yin Shan's](https://stackoverflow.com/a/8921509/930389) code for displaying of Greek symbols.