rem @echo off
del *.class > nul
rem del ..\..\bin\Lambda\*.* > nul
javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Constants.java Console.java Tokeniser.java Parser.java DisplayMessage.java RawConsoleInput.java LambdaExpression.java LambdaName.java LambdaFunction.java LambdaApplication.java ParseException.java IntRef.java BoolRef.java Main.java
java -Xss4m -classpath "..\..\bin;..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar" Lambda.Main