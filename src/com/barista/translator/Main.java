package com.barista.translator;

import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.barista.translator.gen.JavaLexer;
import com.barista.translator.gen.JavaParser;
import com.barista.translator.gen.JavaParser.CompilationUnitContext;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String floc = "js/com/barista/example/HelloWorld.java";
        FileInputStream fis = new FileInputStream(floc);
		ANTLRInputStream input = new ANTLRInputStream(fis);
		
        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		parser.setBuildParseTree(true);
        CompilationUnitContext compilationUnit = parser.compilationUnit();
        /*
         * print out the parse tree, more helpful for debugging.
         */
        /*System.out.println(tree.toStringTree(parser));*/

        ParseTreeUtil tu = new ParseTreeUtil(compilationUnit, tokens, parser);
        CoffeeScriptTranslator translator = new CoffeeScriptTranslator(compilationUnit, tu);
        translator.execute();
        
        String result = translator.result();
        
		System.out.println(result);
	}
}
