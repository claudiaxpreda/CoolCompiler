package cool.compiler;

import java.util.TreeSet;

import cool.structures.DefaultScope;
import cool.structures.FunctionSymbol;
import cool.structures.IdSymbol;
import cool.structures.Scope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

public class FeaturesDefinitionPassVisitor implements ASTVisitor<Void>  {
	protected Scope currentScope = SymbolTable.globals;

	@Override
	public Void visit(Program prog) {
        for (var cl : prog.classes) {
            cl.accept(this);
        }

        return null;
	}

	@Override
	public Void visit(ClassDef classs) {
		String inheritanceCyclesError = "Inheritance cycle for class %s";
		
		if (classs.error) {
			return null;
		}
		
		var type = classs.name;
		var classSymbol = (TypeSymbol) currentScope.lookup(type.getText());
		
		var parents = new TreeSet<String>();
		parents.add(classSymbol.getName());
		
		var parent = classSymbol.getParentClass();
		
		while(parent != null) {
			if (!(parents.add(parent.getName()))) {
            	SymbolTable.error(classs.ctx, type,(String.format(inheritanceCyclesError,
            			type.getText(), parent.getName())));
            	classs.error = true;
            	return  null;
			}
			parent = parent.getParentClass();
		}
		
		
		var classParents = classSymbol.getParents();
		for (var classParent : classParents) {
			for (var child : classSymbol.getChildren()) {
				classParent.addChild(child);
			}
		}

		currentScope = classSymbol;
		
		for (var attr : classs.attributes) {
			attr.accept(this);
		}
		
		for (var meth : classs.methods) {
			meth.accept(this);
		}
		
		currentScope = classSymbol.getParent();
		return null;
	}

	@Override
	public Void visit(VarDef vardef) {
		String illegalSelfError = "Class %s has attribute with illegal name self";
		String attrRedefinedError = "Class %s redefines attribute %s";
		String undefinedTypeErr = "Class %s has attribute %s with undefined type %s";

		var attrId = vardef.def.name;
		var nodeId = vardef.nodeId;
		
		if (attrId.getText().equals("self")) {
			SymbolTable.error(vardef.ctx, attrId, String.format(illegalSelfError,
					((TypeSymbol)currentScope).getName()));
			return null;
		}
		
		var idSymbol = new IdSymbol(attrId.getText());
		if (! currentScope.add(idSymbol)) {
			var symbol  = currentScope.lookup(idSymbol.getName());
			
			if (symbol instanceof IdSymbol) {
				SymbolTable.error(vardef.ctx, attrId, String.format(attrRedefinedError,
						((TypeSymbol)currentScope).getName(),attrId.getText()));
				return null;
			}
		}
		
		vardef.nodeId.setSymbol(idSymbol);;
		nodeId.setScope(currentScope);
		
		var type = vardef.def.type;
		
		var typeSymbol = (TypeSymbol) currentScope.lookup(type.getText());
		
		if(typeSymbol == null) {
			SymbolTable.error(vardef.ctx, type, 
					String.format(undefinedTypeErr, ((TypeSymbol)currentScope).getName(), 
							vardef.def.name.getText(), type.getText()));

		}
		
		
		vardef.nodeId.getSymbol().setType(typeSymbol);
		
	    if (vardef.def.e != null)
            vardef.def.e.accept(this);
		return null;
	}

	@Override
	public Void visit(FuncDef funcdef) {
		String funcRedefinedError = "Class %s redefines method %s";
		String returnTypeError = "Class %s has method %s with undefined return type %s";
		

		var funcId = funcdef.name;
		var nodeId = funcdef.nodeId;
		
		var funcSymbol = new FunctionSymbol(funcId.getText(), currentScope);
		
		if (! currentScope.add(funcSymbol)) {
			
			var symbol  = currentScope.lookup(funcSymbol.getName());
			
			if (symbol instanceof FunctionSymbol) {
				SymbolTable.error(funcdef.ctx, funcId,
					String.format(funcRedefinedError, ((TypeSymbol)currentScope).getName(), 
							funcId.getText()));
				return null;
			}
		}
		
		var typeSymbol =  (TypeSymbol) currentScope.lookup(funcdef.type.getText());
		
		if (typeSymbol == null )  {
			SymbolTable.error(funcdef.ctx, funcId,
					String.format(returnTypeError, ((TypeSymbol)currentScope).getName(),
							funcdef.type.getText()));
			return null;
		}
		
		nodeId.setSymbol(funcSymbol);
		funcSymbol.setType(typeSymbol);
		
		currentScope = funcSymbol;
		for (var formal : funcdef.formals) {
			formal.accept(this);
		}
		
		funcdef.body.accept(this);
	
		currentScope = funcSymbol.getParent();
		return null;
	}

	@Override
	public Void visit(Id id) {
		return null;
	}

	@Override
	public Void visit(Int intt) {
		return null;
	}

	@Override
	public Void visit(TrueVal truee) {
		return null;
	}

	@Override
	public Void visit(FalseVal falsee) {
		return null;
	}

	@Override
	public Void visit(StringC string) {
		return null;
	}

	@Override
	public Void visit(Paren paren) {
		paren.e.accept(this);
		return null;
	}

	@Override
	public Void visit(NotL not) {
		not.e.accept(this);
		return null;
	}

	@Override
	public Void visit(Relational rel) {
		rel.left.accept(this);
		rel.right.accept(this);
		return null;
	}

	@Override
	public Void visit(NotA not) {
		not.e.accept(this);
		return null;
	}

	@Override
	public Void visit(PlusMinus pm) {
		pm.left.accept(this);
		pm.right.accept(this);
		return null;
	}

	@Override
	public Void visit(MultDiv md) {
		md.left.accept(this);
		md.right.accept(this);
		return null;
	}

	@Override
	public Void visit(IsVoid voidd) {
		voidd.e.accept(this);
		return null;
	}

	@Override
	public Void visit(New type) {
		return null;
	}

	@Override
	public Void visit(Formal f) {
		String selfFormalError = "Method %s of class %s has formal parameter with illegal name self";
		String redefinedFormalError = "Method %s of class %s redefines formal parameter %s";
		String illegalTypeFormalError = "Method %s of class %s has formal parameter %s "
				+ "with illegal type SELF_TYPE";
		String undefinedTypeError = "Method %s of class %s has formal parameter %s "
				+ "with undefined type %s";
				
		var classs = (TypeSymbol) currentScope.getParent();
		var method = (FunctionSymbol) currentScope;
		var type = f.type;
		var nodeId = f.nodeId;
		
		if (f.name.getText().equals("self")) {
			SymbolTable.error(f.ctx, f.name, String.format(selfFormalError, method.getName(),
					classs.getName()));
			return null;
		}
		
		if (type.getText().equals("SELF_TYPE")) {
			SymbolTable.error(f.ctx, type, String.format(illegalTypeFormalError, method.getName(),
					classs.getName(), f.name.getText()));
		}
		
		var formalSymbol = new IdSymbol(f.name.getText());
		
		if (! currentScope.add(formalSymbol)) {
			
			SymbolTable.error(f.ctx, f.name, String.format(redefinedFormalError, method.getName(),
					classs.getName(), f.name.getText()));
			return null;

		}
		
		nodeId.setSymbol(formalSymbol);
		nodeId.setScope(currentScope);
		
		var typeSymbol = (TypeSymbol) currentScope.lookup(type.getText());
		
		if (typeSymbol == null) {
			
			SymbolTable.error(f.ctx, type, String.format(undefinedTypeError, method.getName(),
					classs.getName(), f.name.getText(), type.getText()));
			return null;
			
		}
		
		formalSymbol.setType(typeSymbol);
		return null;
	}

	@Override
	public Void visit(Definition def) {
		String illegalNameErr = "Let variable has illegal name self";
		String undefinedTypeErr = "Let variable %s has undefined type %s";
		
		var nodeId = def.nodeId;
		var name = def.name;
		var type = def.type;
		
		nodeId.setScope(currentScope);
		
		if (name.getText().equals("self")) {
			SymbolTable.error(def.ctx, name, String.format(illegalNameErr));
			return null;
		}
		
		var typeSymbol = (TypeSymbol)currentScope.lookup(type.getText());
		
		if (typeSymbol == null) {
			SymbolTable.error(def.ctx, type, String.format(undefinedTypeErr, name.getText(), type.getText()));
			return null;
		}
		
		
		if (def.e != null)
			def.e.accept(this);
		
		var defSymbol = new IdSymbol(name.getText());
		nodeId.setSymbol(defSymbol);
		defSymbol.setType(typeSymbol);
		
		return null;
	}

	@Override
	public Void visit(CaseArg cs) {
		String illegalNameErr = "Case variable has illegal name self";
		String undefinedTypeErr = "Case variable %s has undefined type %s";
		String illegalTypeErr = "Case variable %s has illegal type SELF_TYPE";
		
		var nodeId = cs.nodeId;
		var name = cs.name;
		var type = cs.type;
		
		
		if (name.getText().equals("self")) {
			SymbolTable.error(cs.ctx, name, String.format(illegalNameErr));
			return null;
		}
		
		if (type.getText().equals("SELF_TYPE")) {
			SymbolTable.error(cs.ctx, type, String.format(illegalTypeErr, name.getText()));
			return null;
		}
		var typeSymbol = (TypeSymbol)currentScope.lookup(type.getText());
		
		if (typeSymbol == null) {
			SymbolTable.error(cs.ctx, type, String.format(undefinedTypeErr, name.getText(), type.getText()));
			return null;
		}
		
		var idSymbol = new IdSymbol(cs.name.getText());
		nodeId.setSymbol(idSymbol);
		nodeId.setScope(currentScope);
		idSymbol.setType(typeSymbol);
		
		return null;
	}

	@Override
	public Void visit(Case c) {
		var nodeId = c.nodeId;
		
		
		for (var cond : c.conds ) {
			cond.accept(this);
		}
		
		nodeId.setScope(currentScope);
		return null;
	}

	@Override
	public Void visit(Let let) {
		var nodeId = let.nodeId;
		
		var newScope = new DefaultScope(currentScope);
		nodeId.setScope(newScope);
		
		currentScope = newScope;
		
		for (var def : let.defs ) {
			def.accept(this);
		}
		
		currentScope = newScope.getParent();
		return null;
	}

	@Override
	public Void visit(Brackets br) {
		var nodeId = br.nodeId;
		var newScope = new DefaultScope(currentScope);		
		nodeId.setScope(currentScope);
		
		currentScope = newScope;
		for (var b : br.body) {
			b.accept(this);
		}
		currentScope = newScope.getParent();
		
		return null;
	}

	@Override
	public Void visit(If iff) {
		iff.cond.accept(this);
		iff.thenBranch.accept(this);
		iff.elseBranch.accept(this);
		return null;
	}

	@Override
	public Void visit(While wh) {
		wh.cond.accept(this);
		wh.body.accept(this);
		return null;
	}

	@Override
	public Void visit(Call call) {
		for(var arg : call.args) {
			arg.accept(this);
		}
		return null;
	}

	@Override
	public Void visit(Dispatch at) {
		at.e.accept(this);
		for(var arg : at.args) {
			arg.accept(this);
		}
		return null;
	}

	@Override
	public Void visit(Assign asg) {
		asg.nodeId.accept(this);
		asg.e.accept(this);
		return null;
	}
}
