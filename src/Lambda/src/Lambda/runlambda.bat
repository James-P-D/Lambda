del *.class
rem javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Console.java
rem javac -encoding utf8 Tokeniser.java
rem javac -cp ;. -encoding utf8 Main.java


javac -cp ..\..\libs\jna-5.5.0.jar;..\..\libs\jna-platform-5.5.0.jar;. Console.java -encoding utf8 Tokeniser.java -encoding utf8 Main.java

java Main