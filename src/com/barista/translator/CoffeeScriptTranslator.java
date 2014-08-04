package com.barista.translator;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.barista.translator.gen.JavaBaseVisitor;
import com.barista.translator.gen.JavaParser.ArgumentsContext;
import com.barista.translator.gen.JavaParser.ArrayCreatorRestContext;
import com.barista.translator.gen.JavaParser.ArrayInitializerContext;
import com.barista.translator.gen.JavaParser.BlockContext;
import com.barista.translator.gen.JavaParser.BlockStatementContext;
import com.barista.translator.gen.JavaParser.CatchClauseContext;
import com.barista.translator.gen.JavaParser.CatchTypeContext;
import com.barista.translator.gen.JavaParser.ClassBodyDeclarationBlockContext;
import com.barista.translator.gen.JavaParser.ClassBodyDeclarationContext;
import com.barista.translator.gen.JavaParser.ClassBodyDeclarationMemberDeclarationContext;
import com.barista.translator.gen.JavaParser.ClassBodyDeclarationSemiColonContext;
import com.barista.translator.gen.JavaParser.ClassCreatorRestContext;
import com.barista.translator.gen.JavaParser.ClassDeclarationContext;
import com.barista.translator.gen.JavaParser.CompilationUnitContext;
import com.barista.translator.gen.JavaParser.ConstantExpressionContext;
import com.barista.translator.gen.JavaParser.CreatedNameContext;
import com.barista.translator.gen.JavaParser.CreatorContext;
import com.barista.translator.gen.JavaParser.EnhancedForControlContext;
import com.barista.translator.gen.JavaParser.EnumConstantNameContext;
import com.barista.translator.gen.JavaParser.ExpressionAdditiveContext;
import com.barista.translator.gen.JavaParser.ExpressionBracketsContext;
import com.barista.translator.gen.JavaParser.ExpressionCompareContext;
import com.barista.translator.gen.JavaParser.ExpressionContext;
import com.barista.translator.gen.JavaParser.ExpressionCreatorContext;
import com.barista.translator.gen.JavaParser.ExpressionExplicitGenericInvocationContext;
import com.barista.translator.gen.JavaParser.ExpressionExpressionListContext;
import com.barista.translator.gen.JavaParser.ExpressionIdentifierContext;
import com.barista.translator.gen.JavaParser.ExpressionInnerCreatorContext;
import com.barista.translator.gen.JavaParser.ExpressionListContext;
import com.barista.translator.gen.JavaParser.ExpressionLogicalContext;
import com.barista.translator.gen.JavaParser.ExpressionMultiplicativeContext;
import com.barista.translator.gen.JavaParser.ExpressionPostfixContext;
import com.barista.translator.gen.JavaParser.ExpressionPrefixContext;
import com.barista.translator.gen.JavaParser.ExpressionShiftContext;
import com.barista.translator.gen.JavaParser.ExpressionSuperSuffixContext;
import com.barista.translator.gen.JavaParser.ExpressionTernaryContext;
import com.barista.translator.gen.JavaParser.ExpressionThisContext;
import com.barista.translator.gen.JavaParser.ExpressionTypeExpressionContext;
import com.barista.translator.gen.JavaParser.FieldDeclarationContext;
import com.barista.translator.gen.JavaParser.ForControlContext;
import com.barista.translator.gen.JavaParser.ForInitContext;
import com.barista.translator.gen.JavaParser.ForUpdateContext;
import com.barista.translator.gen.JavaParser.LocalVariableDeclarationContext;
import com.barista.translator.gen.JavaParser.LocalVariableDeclarationStatementContext;
import com.barista.translator.gen.JavaParser.MemberDeclarationContext;
import com.barista.translator.gen.JavaParser.MethodBodyContext;
import com.barista.translator.gen.JavaParser.MethodDeclarationContext;
import com.barista.translator.gen.JavaParser.NonWildcardTypeArgumentsContext;
import com.barista.translator.gen.JavaParser.ParExpressionContext;
import com.barista.translator.gen.JavaParser.PrimitiveTypeContext;
import com.barista.translator.gen.JavaParser.QualifiedNameContext;
import com.barista.translator.gen.JavaParser.StatementAssertContext;
import com.barista.translator.gen.JavaParser.StatementBlockContext;
import com.barista.translator.gen.JavaParser.StatementBreakContext;
import com.barista.translator.gen.JavaParser.StatementContext;
import com.barista.translator.gen.JavaParser.StatementContinueContext;
import com.barista.translator.gen.JavaParser.StatementDoContext;
import com.barista.translator.gen.JavaParser.StatementForContext;
import com.barista.translator.gen.JavaParser.StatementIdentifierContext;
import com.barista.translator.gen.JavaParser.StatementIfContext;
import com.barista.translator.gen.JavaParser.StatementResourceTryContext;
import com.barista.translator.gen.JavaParser.StatementReturnContext;
import com.barista.translator.gen.JavaParser.StatementSemiColonContext;
import com.barista.translator.gen.JavaParser.StatementStatementExpressionContext;
import com.barista.translator.gen.JavaParser.StatementSwitchContext;
import com.barista.translator.gen.JavaParser.StatementSynchronizedContext;
import com.barista.translator.gen.JavaParser.StatementThrowContext;
import com.barista.translator.gen.JavaParser.StatementTryContext;
import com.barista.translator.gen.JavaParser.StatementWhileContext;
import com.barista.translator.gen.JavaParser.SwitchBlockStatementGroupContext;
import com.barista.translator.gen.JavaParser.SwitchLabelContext;
import com.barista.translator.gen.JavaParser.TypeContext;
import com.barista.translator.gen.JavaParser.TypeDeclarationContext;
import com.barista.translator.gen.JavaParser.VariableInitializerContext;

/**
 * TODO to support static methods, we have to be able to recognize them in statements and expressions
 * TODO what is the logic that enables them?
 * @author perico
 *
 */
public class CoffeeScriptTranslator extends JavaBaseVisitor<Void> {

	private static final Object[] EMPTY_OBJ_ARRAY = new Object[] {};

	private ParseTreeUtil tree;

	private CompilationUnitContext compilationUnit;
	
	private int level = 0;

	private StringBuilder sb = new StringBuilder();

	public CoffeeScriptTranslator(CompilationUnitContext compilationUnit, ParseTreeUtil tree) {
		this.compilationUnit = compilationUnit;
		this.tree = tree;
	}

	public String result() {
		return sb.toString();
	}

	public void execute() {
		String pkg = tree.str("//packageDeclaration/qualifiedName");
		List<TypeDeclarationContext> typeDeclaration = compilationUnit.typeDeclaration();
		for (TypeDeclarationContext typeDeclarationContext : typeDeclaration) {
			String cls = tree.str(typeDeclarationContext, "//classDeclaration/Identifier");
			typeDeclaration(pkg, cls, typeDeclarationContext);
			pkg = null; /* set package to null after first use since inner classes wont have packages */
		}
	}

	private void typeDeclaration(String pkg, String cls, TypeDeclarationContext typeDeclarationContext) {
		ClassDeclarationContext classDeclaration = typeDeclarationContext.classDeclaration();
		TypeContext type = classDeclaration.type();
		String ext = type == null ? "" : " extends " + tree.text(type);
		String pkgst = pkg == null ? "" : pkg + ".";
		
		this.iprint("class %s%s%s", pkgst, cls, ext);
		this.nl(2);
		
		level++;
		List<ClassBodyDeclarationContext> classBodyDeclaration = classDeclaration.classBody().classBodyDeclaration();
		
		for (ClassBodyDeclarationContext classBodyDeclarationContext : classBodyDeclaration) {
			classBodyDeclaration(classBodyDeclarationContext);
		}
		level--;
	}

	private void classBodyDeclaration(	ClassBodyDeclarationContext classBodyDeclarationContext) {
		
		if (classBodyDeclarationContext instanceof ClassBodyDeclarationSemiColonContext) {
			// can be ignored
		} else if (classBodyDeclarationContext instanceof ClassBodyDeclarationBlockContext) {
			/* 
			 * blocks in class definition...
			 * DO NOT INDENT 
			 * for now, write static blocks in class scope, ignores non static init blocks
			 * These statements will not be indented since they exist at the same level as fields/methods in coffeescript
			 */
			ClassBodyDeclarationBlockContext block = (ClassBodyDeclarationBlockContext) classBodyDeclarationContext;
			if (block.getChildCount() > 1) {
				List<BlockStatementContext> blockStatement = block.block().blockStatement();
				for (BlockStatementContext blockStatementContext : blockStatement) {
					this.blockStatement(blockStatementContext);
				}
			}
		} else if (classBodyDeclarationContext instanceof ClassBodyDeclarationMemberDeclarationContext) {
			ClassBodyDeclarationMemberDeclarationContext member = (ClassBodyDeclarationMemberDeclarationContext) classBodyDeclarationContext;
			String mods = tree.text(member.modifier());
			this.memberDeclaration(mods.contains("static"), member.memberDeclaration());
		}
		this.nl();
	}
	
	private void memberDeclaration(boolean isStatic, MemberDeclarationContext memberDeclaration) {

		if (memberDeclaration.methodDeclaration() != null) {
			this.methodDeclaration(isStatic, memberDeclaration.methodDeclaration());
		} else if (memberDeclaration.genericMethodDeclaration() != null) {
			
		} else if (memberDeclaration.fieldDeclaration() != null) {
			this.fieldDeclaration(isStatic, memberDeclaration.fieldDeclaration());			
		} else if (memberDeclaration.constructorDeclaration() != null) {
			
		} else if (memberDeclaration.genericConstructorDeclaration() != null) {
		}
	}

	private void methodDeclaration(boolean isStatic, MethodDeclarationContext methodDeclaration) {
		String name = methodDeclaration.Identifier().toString();
		List<String> params = tree.strs(methodDeclaration, "//formalParameter//variableDeclaratorId//Identifier");
		String paramList = tree.join(params, ", ");
		
		this.iprint("%s%s: (%s) ->", isStatic ? "@" : "", name, paramList);
		this.nl();
		level++;
		MethodBodyContext methodBody = methodDeclaration.methodBody();
		List<BlockStatementContext> blockStatement = methodBody.block().blockStatement();
		for (BlockStatementContext blockStatementContext : blockStatement) {
			this.blockStatement(blockStatementContext);
		} 
		
		level--;
	}

	/**
	 * Block, does not indent, required outside.
	 * @param blockStatementContext
	 */
	private void blockStatement(BlockStatementContext blockStatementContext) {
		LocalVariableDeclarationStatementContext localVariableDeclarationStatement = blockStatementContext.localVariableDeclarationStatement();
		StatementContext statement = blockStatementContext.statement();
		TypeDeclarationContext typeDeclaration = blockStatementContext.typeDeclaration();
		if (localVariableDeclarationStatement != null) {
			this.localVariableDeclarationStatement(localVariableDeclarationStatement);
		} else if (statement != null) {
			this.statement(statement);
		} else if (typeDeclaration != null) {
			throw new UnsupportedOperationException("no type declarations allowed in blocks: " + tree.text(typeDeclaration));
		}
	}

	/**
	 * @see #statement(StatementContext, boolean) with indent set to true.
	 * @param statement
	 */
	private void statement(StatementContext statement) {
		this.statement(statement, true);
	}
	
	/**
	 * Indent flag, should this statement indent if it usually indents.
	 * @param statement
	 * @param indent
	 */
	private void statement(StatementContext statement, boolean indent) {
		if (statement instanceof StatementBlockContext) {
			if (indent) {
				level++;	
			}
			StatementBlockContext ctx = (StatementBlockContext) statement;
			List<BlockStatementContext> blockStatement = ctx.block().blockStatement();
			for (BlockStatementContext blockStatementContext : blockStatement) {
				this.blockStatement(blockStatementContext);
			}
			if (indent) {
				level--;	
			}
		} else if (statement instanceof StatementAssertContext) {
			// no assertions in cs
		} else if (statement instanceof StatementIfContext) {
			
			/*
			 * manually indent those cases where child statements are not block statements.
			 */
			
			StatementIfContext ctx = (StatementIfContext) statement;
			List<StatementContext> statements = ctx.statement();
			this.iprint("if ");
			this.parExpression(ctx.parExpression());
			this.nl();
			StatementContext thenstmt = statements.get(0);
			if (thenstmt instanceof StatementBlockContext) {
				this.statement(thenstmt);
			} else {
				level++;
				this.statement(thenstmt);
				level--;
			}

			if (statements.size() > 1) {
				this.iprint("else");
				this.nl();
				StatementContext elsestmt = statements.get(1);
				if (elsestmt instanceof StatementBlockContext) {
					this.statement(elsestmt);
				} else {
					level++;
					this.statement(elsestmt);
					level--;
				}
			}
		} else if (statement instanceof StatementForContext) {
			StatementForContext ctx = (StatementForContext) statement;
			ForControlContext forControl = ctx.forControl();
			this.forControl(forControl, ctx.statement());
		} else if (statement instanceof StatementWhileContext) {
			StatementWhileContext ctx = (StatementWhileContext) statement;
			whileStatement(ctx.parExpression().expression(), ctx.statement());
		} else if (statement instanceof StatementDoContext) {
			StatementDoContext ctx = (StatementDoContext) statement;
			this.statement(ctx.statement(), false);
			this.whileStatement(ctx.parExpression().expression(), ctx.statement());
		} else if (statement instanceof StatementTryContext) {
			
			/*
			 * TODO delegating to cs constructor vars works in a js context, but not in a jvm w java objs context...
			 * TODO keep track of all cross runtime locations for future enhancements
			 */
			
			StatementTryContext ctx = (StatementTryContext) statement;
			this.iprint("try");
			this.level++;
			this.nl();
			String exvar = "__exceptionres";
			List<BlockStatementContext> blockStatement = ctx.block().blockStatement();
			for (BlockStatementContext blockStatementContext : blockStatement) {
				this.blockStatement(blockStatementContext);
			}
			this.level--;
			this.iprint("catch %s", exvar);

			List<CatchClauseContext> catchClause = ctx.catchClause();
			this.nl();
			this.level++;
			this.iprint("switch %s.constructor.name", exvar);
			this.level++;
			for (int i = 0; i < catchClause.size(); i++) {
				CatchClauseContext catchClauseContext = catchClause.get(i);
				CatchTypeContext catchType = catchClauseContext.catchType();
				List<QualifiedNameContext> qnames = catchType.qualifiedName();
				if (i == 0) {
					this.nl();	
				}
				this.iprint("when %s", tree.text(qnames.get(qnames.size() - 1)));
				this.nl();
				BlockContext block = catchClauseContext.block();
				List<BlockStatementContext> bstmts = block.blockStatement();
				this.level++;
				TerminalNode id = catchClauseContext.Identifier();
				this.iprint("%s = %s", id, exvar);
				this.nl();
				for (BlockStatementContext bsctx : bstmts) {
					this.blockStatement(bsctx);
				}
				this.level--;
			}

			this.iprint("else");
			this.nl();
			this.level++;
			this.iprint("throw %s", exvar);
			this.level--;
			this.level--;
			this.level--;
			this.nl();
		} else if (statement instanceof StatementResourceTryContext) {
			throw new UnsupportedOperationException("Resource exceptions are not suppported yet: " + tree.text(statement));
		} else if (statement instanceof StatementSwitchContext) {
			
			/*
			 * TODO should this just be a simple coffeescript switch? 
			 */
			
			StatementSwitchContext ctx = (StatementSwitchContext) statement;
			ExpressionContext expression = ctx.parExpression().expression();
			
			/* evaluate the expression once as an optimization */
			String svar = "__switchres";
			this.iprint(svar + " = ");
			this.expression(expression);
			this.nl();
			
			List<SwitchBlockStatementGroupContext> groups = ctx.switchBlockStatementGroup();
			List<List<SwitchLabelContext>> labelSets = new ArrayList<List<SwitchLabelContext>>();
			List<List<BlockStatementContext>> stmtSets = new ArrayList<List<BlockStatementContext>>();
			boolean iffed = false;
			
			for (SwitchBlockStatementGroupContext group : groups) {
				List<BlockStatementContext> blockStatements = group.blockStatement();
				
				labelSets.add(group.switchLabel());
				stmtSets.add(blockStatements);
				
				if (blockStatements.get(blockStatements.size() - 1).statement() instanceof StatementBreakContext) {
					CASELOOP:
					for (int i = 0, labelMax = labelSets.size(); i < labelMax; i++) {
						
						List<SwitchLabelContext> labels = labelSets.get(i);

						if (iffed) {
							SwitchLabelContext last = labels.get(labels.size() - 1);
							String lastText = tree.text(last.getChild(0));
							if (lastText.equals("default")) {
								this.iprint("else");
							} else {
								this.iprint("else if ");	
							}
						} else {
							this.iprint("if ");
							iffed = true;
						}
						
						for (int j = 0, max = labels.size(), last = max - 1; j < max; j++) {
							SwitchLabelContext slctx = labels.get(j);
							String text = tree.text(slctx);
							ConstantExpressionContext cec = slctx.constantExpression();
							EnumConstantNameContext ecn = slctx.enumConstantName();
							if (ecn != null) {
								throw new UnsupportedOperationException("Enum in switches are not supported: " + text);
							}
							if (cec != null) {
								this.print("%s == ", svar);
								this.expression(cec.expression());
								if (j != last) {
									this.print(" or ");
								}
							}
						}
						
						this.nl();
						this.level++;
						for (int j = i; j < stmtSets.size(); j++) {
							List<BlockStatementContext> stmts = stmtSets.get(j);
							for (BlockStatementContext bsctx : stmts) {
								LocalVariableDeclarationStatementContext localvar = bsctx.localVariableDeclarationStatement();
								StatementContext bsstmt = bsctx.statement();
								TypeDeclarationContext typedecl = bsctx.typeDeclaration();
								if (bsstmt != null && bsstmt instanceof StatementBreakContext) {
									this.level--;
									continue CASELOOP;
								} else if (bsstmt != null) {
									this.statement(bsstmt);
								} else if (localvar != null) {
									this.localVariableDeclarationStatement(localvar);
								} else {
									throw new UnsupportedOperationException("Inline type declarations are not supported: " + tree.text(typedecl));
								}
							}
						}
						this.level--;
					}
					labelSets.clear();
					stmtSets.clear();
				}
			}
		} else if (statement instanceof StatementSynchronizedContext) {
			throw new UnsupportedOperationException("synchronized statements are not supported: " + tree.text(statement));
		} else if (statement instanceof StatementReturnContext) {
			StatementReturnContext ctx = (StatementReturnContext) statement;
			ExpressionContext expression = ctx.expression();
			if (expression != null) {
				this.iprint();
				this.expression(expression);
			} else {
				this.iprint("return");
			}
			this.nl();
		} else if (statement instanceof StatementThrowContext) {
			StatementThrowContext ctx = (StatementThrowContext) statement;
			this.iprint("throw ");
			this.expression(ctx.expression());
			this.nl();
		} else if (statement instanceof StatementBreakContext) {
			StatementBreakContext ctx = (StatementBreakContext) statement;
			if (ctx.Identifier() != null) {
				throw new UnsupportedOperationException("labels are not supported: " + tree.text(statement));
			}
			this.iprint("break");
			this.nl();
		} else if (statement instanceof StatementContinueContext) {
			StatementContinueContext ctx = (StatementContinueContext) statement;
			if (ctx.Identifier() != null) {
				throw new UnsupportedOperationException("labels are not supported: " + tree.text(statement));
			}
			this.iprint("continue");
			this.nl();
		} else if (statement instanceof StatementSemiColonContext) {
		} else if (statement instanceof StatementStatementExpressionContext) {
			StatementStatementExpressionContext ctx = (StatementStatementExpressionContext) statement;
			this.iprint();
			this.expression(ctx.statementExpression().expression());
			this.nl();
		} else if (statement instanceof StatementIdentifierContext) {
			throw new UnsupportedOperationException("labels are not supported: " + tree.text(statement));
		} else {
			this.iprint("%s", tree.text(statement));
			this.nl();
		}
			
	}

	private void whileStatement(ExpressionContext expression, StatementContext statement) {
		this.iprint("while ");
		this.expression(expression);
		this.nl();
		this.statement(statement);
	}

	/**
	 * Pass in statement context because traditional for loops
	 * do not exist in coffeescript so we rewrite them as whiles
	 * enhanced for loops are simpler.
	 * 
	 * @param forControl
	 * @param statementContext
	 */
	private void forControl(ForControlContext forControl, StatementContext statementContext) {
		
		if (forControl.enhancedForControl() != null) {
			EnhancedForControlContext ctx = forControl.enhancedForControl();
			String name = ctx.variableDeclaratorId().Identifier().toString();
			this.iprint("for %s in ", name);
			this.expression(ctx.expression());
			this.nl();
			this.statement(statementContext);
			
		} else {
			
			ForInitContext forInit = forControl.forInit();
			ExpressionContext expression = forControl.expression();
			ForUpdateContext forUpdate = forControl.forUpdate();
			
			if (forInit != null) {
				LocalVariableDeclarationContext varDecl = forInit.localVariableDeclaration();
				ExpressionListContext expressionList = forInit.expressionList();
				if (varDecl != null) {
					this.variableDeclarator(false, varDecl, true);
				} else if (expressionList != null) {
					this.expressionList(expressionList);
				}
			}
			
			this.iprint("while ");
			if (expression != null) {
				this.expression(expression);
			} else {
				this.print("1 == 1");
			}
			this.nl();
			this.statement(statementContext);
			level++;
			this.iprint();
			if (forUpdate != null) {
				ExpressionListContext expList = forUpdate.expressionList();
				this.expressionList(expList);
			}
			level--;
			this.nl();
		}
	}

	/**
	 * Render the expressions.
	 * Does not indent by itself, since expressions are inline.
	 * Outer indentation can be executed with a simple {@link #iprint()} invocation.
	 * @param expression
	 */
	private void expression(ExpressionContext expression) {
		
		if (expression instanceof ExpressionTernaryContext) {
			ExpressionTernaryContext ctx = (ExpressionTernaryContext) expression;
			this.print("if %s then %s else %s", ctx.expression(0), ctx.expression(1), ctx.expression(2));
		} else if (expression instanceof ExpressionIdentifierContext) {
			ExpressionIdentifierContext ctx = (ExpressionIdentifierContext) expression;
			this.expression(ctx.expression());
			this.print(".");
			this.print(ctx.Identifier());
		} else if (expression instanceof ExpressionThisContext) {
			ExpressionThisContext ctx = (ExpressionThisContext) expression;
			this.expression(ctx.expression());
			this.print(".this");
		} else if (expression instanceof ExpressionInnerCreatorContext) {
			/* TODO handler inner creator as it eventually becomes handlers */
			ExpressionInnerCreatorContext ctx = (ExpressionInnerCreatorContext) expression;
			this.expression(ctx.expression());
			this.print(".new ");
			this.print(ctx.innerCreator());
		} else if (expression instanceof ExpressionSuperSuffixContext) {
			/* TODO handle the super suffix which contains arguments and expression list below */
			ExpressionSuperSuffixContext ctx = (ExpressionSuperSuffixContext) expression;
			this.expression(ctx.expression());
			this.print(".");
			this.print(ctx.superSuffix());
		} else if (expression instanceof ExpressionExplicitGenericInvocationContext) {
			/* TODO this also contains a superSuffix */
			ExpressionExplicitGenericInvocationContext ctx = (ExpressionExplicitGenericInvocationContext) expression;
			this.expression(ctx.expression());
			this.print(".");
			this.print(ctx.explicitGenericInvocation());
		} else if (expression instanceof ExpressionBracketsContext) {
			ExpressionBracketsContext ctx = (ExpressionBracketsContext) expression;
			this.expression(ctx.expression(0));
			this.print("[");
			this.expression(ctx.expression(1));
			this.print("]");
		} else if (expression instanceof ExpressionExpressionListContext) {
			ExpressionExpressionListContext ctx = (ExpressionExpressionListContext) expression;
			this.expression(ctx.expression());
			this.print("(");
			ExpressionListContext expList = ctx.expressionList();
			this.expressionList(expList);
			this.print(")");
		} else if (expression instanceof ExpressionPostfixContext) {
			ExpressionPostfixContext ctx = (ExpressionPostfixContext) expression;
			this.expression(ctx.expression());
			this.print(ctx.getChild(1));
		} else if (expression instanceof ExpressionPrefixContext) {
			ExpressionPrefixContext ctx = (ExpressionPrefixContext) expression;
			this.print(ctx.getChild(0));
			this.expression(ctx.expression());
		} else if (expression instanceof ExpressionLogicalContext) {
			ExpressionLogicalContext ctx = (ExpressionLogicalContext) expression;
			this.print(ctx.getChild(0));
			this.expression(ctx.expression());
		} else if (expression instanceof ExpressionMultiplicativeContext) {
			ExpressionMultiplicativeContext ctx = (ExpressionMultiplicativeContext) expression;
			this.expression(ctx.expression(0));
			this.print(ctx.getChild(1));
			this.expression(ctx.expression(1));
		} else if (expression instanceof ExpressionAdditiveContext) {
			ExpressionAdditiveContext ctx = (ExpressionAdditiveContext) expression;
			this.expression(ctx.expression(0));
			this.print(ctx.getChild(1));
			this.expression(ctx.expression(1));
		} else if (expression instanceof ExpressionShiftContext) {
			ExpressionShiftContext ctx = (ExpressionShiftContext) expression;
			this.expression(ctx.expression(0));
			this.print(ctx.getChild(1));
			this.print(ctx.getChild(2));
			if (ctx.children.size() == 5) {
				this.print(ctx.getChild(3));
			}
			this.expression(ctx.expression(1));
		} else if (expression instanceof ExpressionCompareContext) {
			ExpressionCompareContext ctx = (ExpressionCompareContext) expression;
			this.expression(ctx.expression(0));
			this.print(ctx.getChild(1));
			this.expression(ctx.expression(1));
		} else if (expression instanceof ExpressionCreatorContext) {
			
			/*
			 * avoid prefixing an array expression with type information.
			 * let all other types of creation pass through.
			 */
			ExpressionCreatorContext ctx = (ExpressionCreatorContext) expression;
			CreatorContext creator = ctx.creator();
			NonWildcardTypeArgumentsContext nonwcargs = creator.nonWildcardTypeArguments();
			if (nonwcargs != null) {
				this.print(ctx);
			} else {
				ArrayCreatorRestContext arraycreator = creator.arrayCreatorRest();
				CreatedNameContext createdName = creator.createdName();
				if (arraycreator == null) {
					this.print("new ");
					List<TerminalNode> identifier = createdName.Identifier();
					PrimitiveTypeContext primitiveType = createdName.primitiveType();
					if (identifier != null) {
						for (int i = 0, max = identifier.size(), last = max - 1; i < max; i++) {
							this.print(identifier.get(i));
							if (i != last) {
								this.print(".");
							}
						}
					} else {
						this.print(primitiveType);
					}
					ClassCreatorRestContext classCreatorRest = creator.classCreatorRest();
					ArgumentsContext arguments = classCreatorRest.arguments();
					if (arguments != null) {
						/*TODO implement the arguments method as a standalone method*/
						//this.print("(");
						this.print(arguments);
						//this.print(")");
					}
					/*TODO you might have a class body here.. covers new object OR new function case.*/
				} else {
					ArrayInitializerContext arrayinit = arraycreator.arrayInitializer();
					this.arrayInitializer(arrayinit);
				}
			}
		} else if (expression instanceof ExpressionTypeExpressionContext) {
			ExpressionTypeExpressionContext ctx = (ExpressionTypeExpressionContext) expression;
			/* TODO continue with chains */
			/* I believe this is a cast expression */
			this.print(ctx.expression());
		} else {
			String text = tree.text(expression);
			this.print("%s", text);	
		}
	}

	private void print(ParseTree ctx) {
		this.print(tree.text(ctx));
	}

	private void print(ParserRuleContext ctx) {
		this.print(tree.text(ctx));
	}
	
	private void expressionList(ExpressionListContext expList) {
		if (expList != null) {
			List<ExpressionContext> exps = expList.expression();
			for (int i = 0, max = exps.size(), last = max - 1; i < max; i++) {
				ExpressionContext exp = exps.get(i);
				this.expression(exp);
				if (i != last) {
					this.print(",");
				}
			}
		}
	}
	
	/**
	 * Generate the array initializers which may bounce back between expressions.
	 * @param arrayinit
	 */
	private void arrayInitializer(ArrayInitializerContext arrayinit) {
		this.print("[");
		List<VariableInitializerContext> varinits = arrayinit.variableInitializer();
		for (int i = 0, max = varinits.size(), last = max - 1; i < max; i++) {
			VariableInitializerContext varinit = varinits.get(i);
			if (varinit.arrayInitializer() != null) {
				this.arrayInitializer(varinit.arrayInitializer());
			} else {
				this.expression(varinit.expression());
			}
			if (i != last) {
				this.print(", ");
			}
		}
		this.print("]");
	}

	private void parExpression(ParExpressionContext parExpression) {
		this.print("(");
		this.expression(parExpression.expression());
		this.print(")");
	}

	private void localVariableDeclarationStatement(LocalVariableDeclarationStatementContext localVariableDeclarationStatement) {
		this.variableDeclarator(false, localVariableDeclarationStatement, true);
	}

	private void fieldDeclaration(boolean isStatic, FieldDeclarationContext field) {
		this.variableDeclarator(isStatic, field, false);
	}

	/**
	 * Handles fields and local variables which si why static is a parameter..
	 * @param isStatic
	 * @param field
	 */
	private void variableDeclarator(boolean isStatic, ParserRuleContext field, boolean isVar) {
		List<ParseTree> pts = tree.trees(field, "//variableDeclarator");
		for (ParseTree pt : pts) {
			String name = tree.str(pt, "//variableDeclaratorId");
			String at = isStatic ? "@" : "";
			String op = isVar ? " =" : ":";
			/*
			 * TODO this line currently works, but should be handled by an
			 * expression. see variableInitializer in grammar
			 */
			ParseTree value = tree.tree(pt, "//variableInitializer");
			this.iprint("%s%s%s ", at, name, op);
			this.variableInitializer((VariableInitializerContext) value);
			this.nl();
		}
	}

	/**
	 * Used to continue the chain of parsing.
	 * Beware of any statement which might contain a child expression or statement.
	 * @param var
	 */
	private void variableInitializer(VariableInitializerContext var) {
		if (var == null) {
			this.print("null");
			return;
		} else if (var.arrayInitializer() != null) {
			this.arrayInitializer(var.arrayInitializer());
			return;
		} else if (var.expression() != null) {
			this.expression(var.expression());
			return;
		} 
		throw new UnsupportedOperationException("Unhandled type of variable initializer: " + tree.text(var));
	}

	/**
	 * Convenience wrapper
	 * 
	 * @param format
	 * @param args
	 */
	private void iprint(String format, Object... args) {
		this.indent();
		this.print(format, args);
	}

	private void iprint(String format) {
		this.iprint(format, EMPTY_OBJ_ARRAY);
	}

	
	private void iprint() {
		this.iprint("");
	}
	
	private void print(String format, Object... args) {
		sb.append(String.format(format, args));
	}
	
	private void print(String format) {
		this.print(format, EMPTY_OBJ_ARRAY);
	}
	
	/**
	 * Indent by the appropriate number of levels
	 */
	private void indent() {
		for (int i = 0; i < level; i++) {
			sb.append("  ");
		}
	}

	/**
	 * Add a newline to the output
	 */
	private void nl() {
		sb.append("\n");
	}

	/**
	 * Add two newlines
	 */
	private void nl(int count) {
		for (int i = 0; i < count; i++) {
			this.nl();
		}
	}
}
