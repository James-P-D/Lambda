rem @echo off
del *.class > nul
rem del C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin\Lambda\*.* > nul

javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Constants.java Console.java Tokeniser.java Parser.java DisplayMessage.java RawConsoleInput.java LambdaExpression.java LambdaName.java LambdaFunction.java LambdaApplication.java ParseException.java IntRef.java BoolRef.java Main.java
rem start java -Xss4m -classpath "C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin;..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar" Lambda.Main booleans.lbd maths.lbd conditionals.lbd tuples.lbd lists.lbd functions.lbd
java -Xss4m -classpath "C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin;..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar" Lambda.Main
