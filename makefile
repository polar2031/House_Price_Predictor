all:
	javac -cp "lib/*" *.java
	java -cp lib/*:. HousePricePredictor
