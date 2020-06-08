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
    2. [Default Definitions](#Default-Definitions)
    3. [Loading Files](#Loading-Files)    
    4. [Debug Mode](#Debug-Mode)
    5. [Alpha Mode](#Alpha-Mode)
    6. [Quitting](#Quitting)
3. [Building Notes and Problems](#Building-Notes-and-Problems)
4. [Acknowledgements](#Acknowledgements)
    
### Lambda Calculus

If you already understand the Lambda Calculus, you can skip this section and head straight to [The Application](#The-Application).  

Definition

```
<expression>  = <name> | <function> | <application>
<function>    = λ<name>.<expression>
<application> = <expression> <expression>
```

### History

### Beta Reduction

### Alpha Equivalence

### The Application

### Help

### Default Definitions

### Loading Files

### Debug Mode

### Alpha Mode

### Quitting

Finally, you can terminate the program by entering `quit`, `exit` or pressing the <kbd>ESC</kbd> key.

### Building Notes and Problems

The building instructions can be found [runlambda.bat](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/runlambda.bat) and are also included below:

```
javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Constants.java Console.java Tokeniser.java RawConsoleInput.java -encoding utf8 Main.java
java -classpath "C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin;..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar" Lambda.Main C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\src\Lambda\definitions.lbd
```

A fair amount of work went into getting the console I/O to work and display nicely. Since Java is fussy about support single-keypress-input, displaying Unicode characters (`λ`, `α`, etc.) and using colours, we need to use a bunch of Windows-specific stuff to achieve this. If you are struggling with inputting strings, or are seeing strange, non-printable values in the output, simply set `FANCY_UI` to `false` in [Console.Java](https://github.com/James-P-D/Lambda/blob/master/src/Lambda/src/Lambda/Console.java).

### Acknowledgements

The application uses [RawConsoleInput](https://www.source-code.biz/snippets/java/RawConsoleInput/) courtesy of [Christian d'Heureuse](https://stackoverflow.com/questions/1066318/how-to-read-a-single-char-from-the-console-in-java-as-the-user-types-it/30008252#30008252) for the single-character-input, and [Yin Shan's](https://stackoverflow.com/a/8921509/930389) code for displaying UTF8 characters.