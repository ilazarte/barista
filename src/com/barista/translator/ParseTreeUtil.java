package com.barista.translator;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.pattern.ParseTreeMatch;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.antlr.v4.runtime.tree.xpath.XPath;

/**
 * Consider opengl-ish state based api?
 * That way you can push trees yourself.
 * @author perico
 *
 */
public class ParseTreeUtil {

	private ParseTree tree;
	
	private CommonTokenStream tokens;

	private Parser parser;
	
	public ParseTreeUtil(ParseTree tree, CommonTokenStream tokens, Parser parser) {
		super();
		this.tree = tree;
		this.tokens = tokens;
		this.parser = parser;
	}
	
	private ParseTree isTree;
	
	private int isRule;
	
	/**
	 * Used with is, sets a tree and rule declaration to be used for is tests.
	 * @param tree
	 * @param rule
	 */
	public ParseTreeUtil with(ParseTree tree, int rule) {
		this.isTree = tree;
		this.isRule = rule;
		return this;
	}

	/**
	 * if (xpath.is(tree, "static <block>", JavaParser.RULE_classBodyDeclaration)) {
	 *   execute(tree);
	 * } 
	 * @param pattern
	 * @param rule
	 * @return
	 */
	public boolean is(String pattern) {
		ParseTreePattern ptp = parser.compileParseTreePattern(pattern, isRule);
		ParseTreeMatch match = ptp.match(isTree);
		return match.succeeded();
	}
	
	/**
	 * Return the first result
	 * @param xpath
	 * @return
	 */
	public String str(String xpath) {
		return this.str(tree, xpath);
	}

	/**
	 * First result relative to parseTree
	 * @param parseTree
	 * @param xpath
	 * @return
	 */
	public String str(ParseTree parseTree, String xpath) {
		String value = null;
		for (ParseTree pt : XPath.findAll(parseTree, xpath, parser) ) {
		    value = this.text(pt);
		    break;
		}
		return value;
	}

	/**
	 * Return a list of values
	 * @param xpath
	 * @return
	 */
	public List<String> strs(String xpath) {
		return strs(tree, xpath);
	}
	
	/**
	 * Return a list of values
	 * @param xpath
	 * @return
	 */
	public List<String> strs(ParseTree parseTree, String xpath) {
		List<String> values = new ArrayList<String>();
		for (ParseTree pt : XPath.findAll(parseTree, xpath, parser) ) {
		    values.add(this.text(pt));
		}
		return values;
	}
	
	public ParseTree tree(String xpath) {
		return tree(tree, xpath);
	}
	
	public ParseTree tree(ParseTree parseTree, String xpath) {
		ParseTree value = null;
		for (ParseTree pt : XPath.findAll(parseTree, xpath, parser) ) {
		    value = pt;
		    break;
		}
		return value;
	}

	public List<ParseTree> trees(String xpath) {
		return this.trees(tree, xpath);
	}
	
	public List<ParseTree> trees(ParseTree parseTree, String xpath) {
		List<ParseTree> values = new ArrayList<ParseTree>();
		for (ParseTree pt : XPath.findAll(parseTree, xpath, parser) ) {
		    values.add(pt);
		}
		return values;
	}
	
	/**
	 * Return a list of matches.
	 * 
	 * ie:
	 * 	rule("//classBodyDeclaration", "static <block>", JavaParser.RULE_classBodyDeclaration);
	 * 
	 * @param xpath
	 * @param pattern
	 * @param rule
	 * @return
	 */
	public List<ParseTreeMatch> matches(String xpath, String pattern, int rule) {
		ParseTreePattern p = parser.compileParseTreePattern(pattern, rule);
		List<ParseTreeMatch> matches = p.findAll(tree, xpath);
		return matches;
	}

	public String text(List<? extends ParserRuleContext> contexts) {
		StringBuilder sb = new StringBuilder();
		for (ParserRuleContext parserRuleContext : contexts) {
			sb.append(this.text(parserRuleContext));
		}
		String string = sb.toString();
		return string;
	}
	
	public String text(ParseTreeMatch match) {
		return text(match.getTree());
	}
	
	public String text(ParseTree tree) {
		return tokens.getText(tree.getSourceInterval());
	}
	
	public String text(ParserRuleContext context) {
		return tokens.getText(context);
	}

	/**
	 * Join a list by a seperator
	 * @param params
	 * @param string
	 * @return
	 */
	public String join(List<String> params, String sep) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0, max = params.size(), last = max - 1; i < max; i++) {
			sb.append(params.get(i));
			if (i != last) {
				sb.append(sep);
			}
		}
		return sb.toString();
	}
}
