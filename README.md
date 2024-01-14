# Lox

In this repo I'm learning about compilers by reading [Crafting Interpreters](https://craftinginterpreters.com/) and implementing the Lox language.

## Grammar

```
expression  -> comma ;
comma       -> equality ( "," equality )* ;
equality    -> comparison ( ( "!=" | "==" ) comparison )* ;
comparison  -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term        -> factor ( ( "-" | "+" ) factor )* ;
factor      -> unary ( ( "/" | "*" ) unary )* ;
unary       -> ( "!" | "-" ) unary | primary ;
primary     -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;
```
