#!/usr/bin/env python3

import sys
from os import path


def define_ast(output_dir, base_name, types):
    output_file = path.join(output_dir, base_name + ".java")
    with open(output_file, "w") as f:
        f.write("package lox;\n\n")
        f.write("import java.util.List;\n\n")
        f.write(f"abstract class {base_name} {{\n")

        f.write("    abstract <R> R accept(Visitor<R> visitor);\n\n");
        f.write("    interface Visitor<R> {\n")
        for type_name, fields in types:
            f.write(f"        R visit{type_name}{base_name}({type_name} {base_name.lower()});\n");
        f.write("    }\n")

        for type_name, fields in types:
            f.write(f"\n    static class {type_name} extends {base_name} {{\n")
            for field in fields.split(","):
                f.write(f"        final {field.strip()};\n")
            f.write(f"\n        {type_name}({fields}) {{\n")
            for field in fields.split(","):
                field_name = field.strip().split(" ")[1]
                f.write(f"            this.{field_name} = {field_name};\n")
            f.write("        }\n\n")
            f.write("        @Override\n")
            f.write("        <R> R accept(Visitor<R> visitor) {\n")
            f.write(f"            return visitor.visit{type_name}{base_name}(this);\n")
            f.write("        }\n")
            f.write("    }\n")

        f.write("}\n")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print(f"Usage: generate_ast <output directory>")
        sys.exit(64)

    output_dir = path.abspath(sys.argv[1])

    define_ast(output_dir, "Expr", [
        ("Binary", "Expr left, Token operator, Expr right"),
        ("Grouping", "Expr expression"),
        ("Literal", "Object value"),
        ("Unary", "Token operator, Expr right"),
    ])
