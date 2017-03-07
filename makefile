all:
	javac -cp "lib/*" *.java
	jar -cvfm House_Price_Predictor.jar MANIFEST.MF ./
	java -jar House_Price_Predictor.jar
compile:
	javac -cp "lib/*" *.java
test:
	javac -cp "lib/*" *.java
	java -cp lib/*:. HousePricePredictor
