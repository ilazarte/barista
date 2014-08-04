package com.barista.poc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import com.barista.translator.gen.JavaLexer;
import com.barista.translator.gen.JavaParser;

/**
 * Novel approach suggested by this guy:
 * http://stackoverflow.com/questions/20541073/translation-of-pl-sql-code-to-java-using-antlr-4-and-stringtemplate-4
 * @author perico
 *
 */

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		String src = "demo/com/barista/demo/StringFile.java";
		//src = "demo/com/barista/demo/MessageCls.java";
		
		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(src));
		JavaLexer lexer = new JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		parser.setBuildParseTree(true);
		ParseTree tree = parser.compilationUnit();

		STGroupFile stg = new STGroupFile("src/com/barista/translator/CoffeeScriptReflect.stg");
		stg.registerModelAdaptor(ParserRuleContext.class, new ContextModelAdapter());
		ST t = stg.getInstanceOf("compilationUnit");
		t.add("self", tree);
		System.out.println(t.render());
	}
}
