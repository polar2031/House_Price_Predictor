javac -cp "lib/*" *.java
jar -cvfm test.jar MANIFEST.MF ./
java -jar test.jar
