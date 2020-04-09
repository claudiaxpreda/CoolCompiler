package cool.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import cool.structures.IdSymbol;
import cool.structures.Scope;

import java.util.*;


public abstract class ASTNode {
	public <T> T accept(ASTVisitor<T> visitor) {
		return null;
	}
}

abstract class Expression extends ASTNode {
	Token token;
	ParserRuleContext ctx;

	Expression(Token token) {
		this.token = token;
	}
	Expression(Token token, ParserRuleContext ctx) {
		this.token = token;
		this.ctx = ctx;
	}
}

abstract class Feature extends ASTNode {
	Token token;
	ParserRuleContext ctx;
	Id nodeId;

	Feature(Token token, ParserRuleContext ctx, Id nodeId) {
		this.token = token;
		this.ctx = ctx;
		this.nodeId = nodeId;
	}
}

abstract class Operators extends Expression {
	Expression left;
	Expression right;
	Token op;

	Operators(Expression left, Expression right, Token op, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.left = left;
		this.right = right;
		this.op = op;
	}
}

class Id extends Expression {
	private Scope scope;
	private IdSymbol symbol;
	
	 Id(Token token) {
		 super(token);
	 }
	
	Id(Token token, ParserRuleContext ctx) {
		super(token, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
	
    IdSymbol getSymbol() {
        return symbol;
    }

    void setSymbol(IdSymbol symbol) {
        this.symbol = symbol;
    }
    
    Scope getScope() {
        return scope;
    }

    void setScope(Scope scope) {
        this.scope = scope;
    }
    
}

class Program extends ASTNode {
	final ArrayList<ClassDef> classes = new ArrayList<ClassDef>();

	Program() {
	}

	void addClasses(ClassDef coolClass) {
		classes.add(coolClass);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class ClassDef extends ASTNode {
	final ArrayList<FuncDef> methods = new ArrayList<FuncDef>();
	final ArrayList<VarDef> attributes = new ArrayList<VarDef>();
	Token name;
	Token parentType;
	ParserRuleContext ctx;
	Boolean error = false;

	ClassDef(Token name, Token type, ParserRuleContext ctx) {
		this.name = name;
		this.parentType = type;
		this.ctx = ctx;
	}

	void addMethod(FuncDef method) {
		methods.add(method);
	}

	void addAttribute(VarDef attr) {
		attributes.add(attr);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class VarDef extends Feature {
	Definition def;
	VarDef(Definition def, Token start, ParserRuleContext ctx, Id nodeId) {
		super(start, ctx, nodeId);
		this.def = def;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Formal extends Expression {
	Token name;
	Token type;
	Id nodeId;

	Formal(Token name, Token type, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.name = name;
		this.type = type;
		this.nodeId = new Id(name);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Definition extends Expression {
	Token name;
	Token type;
	Expression e;
	Id nodeId;

	Definition(Token name, Token type, Expression e, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.name = name;
		this.type = type;
		this.e = e;
		this.nodeId = new Id(name);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class FuncDef extends Feature {
	Token name;
	Token type;
	Expression body;

	final ArrayList<Formal> formals = new ArrayList<Formal>();

	FuncDef(Token name, Token type, Expression body, Token start, ParserRuleContext ctx, Id nodeId) {
		super(start, ctx, nodeId);
		this.name = name;
		this.type = type;
		this.body = body;
	}

	void addArgs(Formal arg) {
		formals.add(arg);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class TrueVal extends Expression {
	TrueVal(Token token, ParserRuleContext ctx) {
		super(token, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class FalseVal extends Expression {
	FalseVal(Token token, ParserRuleContext ctx) {
		super(token, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Int extends Expression {

	Int(Token token, ParserRuleContext ctx) {
		super(token, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class StringC extends Expression {

	StringC(Token token, ParserRuleContext ctx) {
		super(token, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Paren extends Expression {
	Expression e;

	Paren(Expression e, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.e = e;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class NotL extends Expression {
	Expression e;

	NotL(Expression e, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.e = e;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Relational extends Operators {
	Relational(Expression left, Expression right, Token op, Token start, ParserRuleContext ctx) {
		super(left, right, op, start, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}

}

class NotA extends Expression {
	Expression e;

	NotA(Expression e, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.e = e;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class PlusMinus extends Operators {
	PlusMinus(Expression left, Expression right, Token op, Token start, ParserRuleContext ctx) {
		super(left, right, op, start, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class MultDiv extends Operators {
	MultDiv(Expression left, Expression right, Token op, Token start, ParserRuleContext ctx) {
		super(left, right, op, start, ctx);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}

}

class IsVoid extends Expression {
	Expression e;

	IsVoid(Expression e, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.e = e;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class New extends Expression {
	Token type;

	New(Token type, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.type = type;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class CaseArg extends Expression {
	Token name;
	Token type;
	Expression init;
	Id nodeId;

	CaseArg(Token name, Token type, Expression init, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.name = name;
		this.type = type;
		this.init = init;
		this.nodeId = new Id(name);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}

}

class Case extends Expression {
	Expression e;
	final ArrayList<CaseArg> conds = new ArrayList<CaseArg>();
	final String caseBranch = "case branch";
	Token key;
	Id nodeId;

	Case(Expression e, Token key, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.e = e;
		this.key = key;
		this.nodeId = new Id(start);
	}

	void addConds(CaseArg cond) {
		conds.add(cond);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Let extends Expression {
	Expression e;
	Token key;
	Id nodeId;
	final ArrayList<Definition> defs = new ArrayList<Definition>();
	final String local = "local";

	Let(Expression e, Token key, Token start, ParserRuleContext ctx, Id nodeId) {
		super(start, ctx);
		this.e = e;
		this.key = key;
		this.nodeId = nodeId;
	}

	void addConds(Definition cond) {
		defs.add(cond);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Brackets extends Expression {
	final ArrayList<Expression> body = new ArrayList<Expression>();
	final String block = "block";
	Id nodeId;

	Brackets(Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.nodeId = new Id(ctx.start);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}

	void addExpr(Expression e) {
		body.add(e);
	}
}

class If extends Expression {
	Expression cond;
	Expression thenBranch;
	Expression elseBranch;
	Token key;

	If(Expression cond, Expression thenBranch, Expression elseBranch, Token key, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.cond = cond;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
		this.key = key;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class While extends Expression {
	Expression cond;
	Expression body;
	Token key;

	While(Expression cond, Expression body, Token key, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.cond = cond;
		this.body = body;
		this.key = key;
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Call extends Expression {
	Token name;
	List<Expression> args = new ArrayList<Expression>();

	Call(Token name, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.name = name;
	}

	void addArg(Expression arg) {
		args.add(arg);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Dispatch extends Expression {
	Token type;
	Token id;
	Expression e;
	List<Expression> args = new ArrayList<Expression>();

	Dispatch(Expression e, Token type, Token id, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.e = e;
		this.type = type;
		this.id = id;
	}

	void addArg(Expression arg) {
		args.add(arg);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}

class Assign extends Expression {
	Token name;
	Expression e;
	Id nodeId;

	Assign(Token name, Expression e, Token start, ParserRuleContext ctx) {
		super(start, ctx);
		this.name = name;
		this.e = e;
		this.nodeId = new Id(name);
	}

	public <T> T accept(ASTVisitor<T> visitor) {
		return visitor.visit(this);
	}
}