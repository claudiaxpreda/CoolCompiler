package cool.compiler;

import cool.parser.CoolParser;
import cool.parser.CoolParserBaseVisitor;

public class ASTConstructionVisitor extends CoolParserBaseVisitor<ASTNode>{
	
    public ASTNode visitId(CoolParser.IdContext ctx) {
        return new Id(ctx.ID().getSymbol(), ctx);
    }
    
    public ASTNode visitInt(CoolParser.IntContext ctx) {
    	return new Int(ctx.INT().getSymbol(), ctx);
    }
    
    public ASTNode visitString(CoolParser.StringContext ctx) {
    	return new StringC(ctx.STRING().getSymbol(), ctx);
    }
    
    public ASTNode visitTrue(CoolParser.TrueContext ctx) {
    	return new TrueVal(ctx.TRUE().getSymbol(), ctx);
    }
    
    public ASTNode visitFalse(CoolParser.FalseContext ctx) {
    	return new FalseVal(ctx.FALSE().getSymbol(), ctx);
    }
    
    public ASTNode visitParen(CoolParser.ParenContext ctx) {
		return new Paren((Expression)visit(ctx.e), ctx.start, ctx);
	}
    public ASTNode visitNotL(CoolParser.NotLContext ctx) {
    	return new NotL((Expression)visit(ctx.e), ctx.start, ctx);
	}
    public ASTNode visitNotA(CoolParser.NotAContext ctx) {
    	return new NotA((Expression)visit(ctx.e), ctx.start, ctx);
	}
    
    public ASTNode visitIsVoid(CoolParser.IsVoidContext ctx) {
    	return new IsVoid((Expression)visit(ctx.e), ctx.start, ctx);
	}
   	
    public ASTNode visitNewType(CoolParser.NewTypeContext ctx) {
    	return new New(ctx.type, ctx.start, ctx);
	}
    
    public ASTNode visitFormal(CoolParser.FormalContext ctx) {
    	return new Formal(ctx.name, ctx.type, ctx.start, ctx);
	}
    
    
    public ASTNode visitVarDef(CoolParser.VarDefContext ctx) {
    	return new VarDef((Definition)visit(ctx.def()), ctx.start, ctx, 
    			new Id(ctx.def().name));
    }
    
    public ASTNode visitFuncDef(CoolParser.FuncDefContext ctx) {
        var result = new FuncDef(ctx.name, ctx.type,
        		(Expression)visit(ctx.body), ctx.start, ctx,
        		new Id(ctx.name));
        
    	for (var f : ctx.formals){
    		result.formals.add((Formal)visit(f));
    	}
        return result;
    }
    
    public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
        var result = new Program();
        for (var child : ctx.children) {
        	var visited = visit(child);
        	if (visited  != null)
        		result.classes.add((ClassDef)visited);
        }
        return result;
    }
    
    public ASTNode visitCoolClass(CoolParser.CoolClassContext ctx) {
        var result = new ClassDef(ctx.name, ctx.parentType, ctx);
        for (var child : ctx.children) {
        	var visited = visit(child);
        	if (visited  != null) {
        		if (visited instanceof FuncDef) {
        			result.methods.add((FuncDef)visited);
        		}
        		if (visited instanceof VarDef) {
        			result.attributes.add((VarDef)visited);
        		}
        	}
        }
        return result;
    }
    
	public ASTNode visitRelational(CoolParser.RelationalContext ctx) {
		return new Relational((Expression)visit(ctx.left),
							(Expression)visit(ctx.right),
							ctx.op,
							ctx.start,
							ctx);
	}
	
	public ASTNode visitPlusMinus(CoolParser.PlusMinusContext ctx) {
		return new PlusMinus((Expression)visit(ctx.left),
							(Expression)visit(ctx.right),
							ctx.op,
							ctx.start,
							ctx);	
	}
	
	public ASTNode visitMultDiv(CoolParser.MultDivContext ctx) {
		return new MultDiv((Expression)visit(ctx.left),
							(Expression)visit(ctx.right),
							ctx.op,
							ctx.start,
							ctx);	
	}
	
    public ASTNode visitDef(CoolParser.DefContext ctx) {
    	Expression e = null;
    	if (ctx.e != null) {
    		e =(Expression) visit(ctx.e);
    	}
    	return new Definition(ctx.name, ctx.type, e, ctx.start, ctx);
    }
    
    public ASTNode visitCsarg(CoolParser.CsargContext ctx) {
    	return new CaseArg(ctx.name, ctx.type, 
    			(Expression)visit(ctx.init), ctx.start, ctx);
	}
    
    public ASTNode visitCase(CoolParser.CaseContext ctx) {
    	var result = new Case((Expression)visit(ctx.e), 
    			ctx.CASE().getSymbol(), ctx.start, ctx);
    	
    	for (var arg : ctx.conds) {
    		result.addConds((CaseArg)visit(arg));
    	}
    	
    	return result;
    }
    
    public ASTNode visitLet(CoolParser.LetContext ctx) {
    	var result = new Let((Expression)visit(ctx.e),
    			ctx.LET().getSymbol(), ctx.start, ctx, new Id(ctx.start));
    	
    	for (var def : ctx.defs) {
    		result.addConds((Definition)visit(def));
    	}
    	
    	return result;
    }
    
    public ASTNode visitCall(CoolParser.CallContext ctx) {
    	var result = new Call(ctx.name, ctx.start, ctx);
    	for (var arg : ctx.args) {
    		result.addArg((Expression)visit(arg));
    	}
    	return result;
    }
    
    
    public ASTNode visitDispatch(CoolParser.DispatchContext ctx) {
    	var result = new Dispatch((Expression)visit(ctx.e),
    			ctx.type, ctx.id,ctx.start, ctx);  	
    	for (var arg : ctx.args) {
    		result.addArg((Expression)visit(arg));
    	}
    	return result;
    }
    
    public ASTNode visitAssign(CoolParser.AssignContext ctx) {
    	return new Assign(ctx.name, (Expression)visit(ctx.e), ctx.start, ctx);
    }
    
    public ASTNode visitIf(CoolParser.IfContext ctx) {
    	return new If((Expression)visit(ctx.cond),
    				(Expression)visit(ctx.thenBranch),
    				(Expression)visit(ctx.elseBranch),
    				ctx.IF().getSymbol(),
    				ctx.start,
    				ctx);
    }
    
    public ASTNode visitWhile(CoolParser.WhileContext ctx) {
    	return new While((Expression)visit(ctx.cond),
    				(Expression)visit(ctx.body),
    				ctx.WHILE().getSymbol(),
    				ctx.start,
    				ctx);
    }
    
    public ASTNode visitBrackets(CoolParser.BracketsContext ctx) {
    	var result = new Brackets(ctx.start, ctx);
    	
    	for (var e : ctx.body) {
    		result.addExpr((Expression)visit(e));
    	}
    	return result;
    }
}
