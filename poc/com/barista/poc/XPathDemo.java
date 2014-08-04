package com.barista.poc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.pattern.ParseTreeMatch;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.antlr.v4.runtime.tree.xpath.XPath;

import com.barista.translator.gen.JavaLexer;
import com.barista.translator.gen.JavaParser;

/**
 * Very cool stuff.
 * https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Parse+Tree+Matching+and+XPath
 * @author perico
 */
public class XPathDemo {

	private CommonTokenStream tokens;
	
	public static void main(String[] args) throws IOException {
		XPathDemo xpath = new XPathDemo();
		xpath.execute();
	}

	private void execute() throws IOException {
		InputStream is = new FileInputStream("demo/com/barista/demo/StringFile.java");
		ANTLRInputStream input = new ANTLRInputStream(is);
		JavaLexer lexer = new JavaLexer(input);
		tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		parser.setBuildParseTree(true);

		ParseTree tree = parser.compilationUnit();

		System.out.println("Package");
		for (ParseTree pt : XPath.findAll(tree, "//packageDeclaration/qualifiedName", parser) ) {
		    System.out.println("\t" + this.text(pt));
		}
		
		System.out.println("Classname");
		for (ParseTree pt : XPath.findAll(tree, "//classDeclaration/Identifier", parser) ) {
		    System.out.println("\t" + this.text(pt));
		}		
				
		System.out.println("Pure xpath query, find all statements");
		String xpath = "//blockStatement/*";
		for (ParseTree pt : XPath.findAll(tree, "//statement", parser) ) {
		    System.out.println("\t" + this.text(pt));
		}
		
		/*
		 * Find all int assignment statements in blocks.
		 * The parse tree patterh is a way to select "types" of statements within an xpath.
		 */
		System.out.println("Find all int statements in blocks");
		String treePattern = "int <Identifier> = <expression>;";
		ParseTreePattern pattern = parser.compileParseTreePattern(treePattern, JavaParser.RULE_localVariableDeclarationStatement);
		List<ParseTreeMatch> matches = pattern.findAll(tree, xpath);
		printMatches(tokens, matches);

		/*
		 * Can also use findAll
		 */
		System.out.println("Find all assignment statements in blocks.");
		pattern = parser.compileParseTreePattern("String <Identifier> = <expression>;", JavaParser.RULE_localVariableDeclarationStatement);
		matches = pattern.findAll(tree, xpath);
		for (ParseTreeMatch match : matches) {
			String id = this.text(match.get("Identifier"));
			String exp = this.text(match.get("expression"));
			String text = this.text(match);
			System.out.printf("\tID: %s, EXP: %s, TEXT:%s%n", id, exp, text);
		}
		
		printMatches(tokens, matches);
	}
	
	private String text(ParseTreeMatch match) {
		return text(match.getTree());
	}
	
	private String text(ParseTree tree) {
		return tokens.getText(tree.getSourceInterval());
	}
	
	private void printMatches(CommonTokenStream tokens,
			List<ParseTreeMatch> matches) {
		for (ParseTreeMatch match : matches) {
			String text = tokens.getText(match.getTree().getSourceInterval());
			System.out.println("\t" + text);
		}
	}
}
