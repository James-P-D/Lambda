@echo off
del *.class > nul

rem ORIGINAL FROM ECLIPSE:
rem "C:\Program Files\Java\jre1.8.0_251\bin\javaw.exe" -agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:21341 -Dfile.encoding=UTF-8 -classpath "C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin;C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\libs\jna-platform-5.5.0.jar;C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\libs\jna-5.5.0.jar" Lambda.Main

rem WORKING:
rem javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Console.java -encoding utf8 Tokeniser.java -encoding utf8 Main.java
rem java -classpath "C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin;C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\libs\jna-platform-5.5.0.jar;C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\libs\jna-5.5.0.jar" Lambda.Main
rem java -classpath "C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin;..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar" Lambda.Main

rem OLD
rem javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Console.java
rem javac -encoding utf8 Tokeniser.java
rem javac -cp ;. -encoding utf8 Main.java

javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Constants.java Console.java Tokeniser.java Parser.java DisplayMessage.java RawConsoleInput.java LambdaExpression.java LambdaName.java LambdaFunction.java LambdaApplication.java ParseException.java IntRef.java Main.java
java -Xss4m -classpath "C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\bin;..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar" Lambda.Main C:\Users\jdorr\Desktop\Dev\Lambda\src\Lambda\src\Lambda\definitions.lbd
