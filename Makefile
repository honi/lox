repl:
	javac lox/Lox.java
	java lox.Lox

clean:
	rm lox/*.class

.PHONY: clean repl
