package cool.compiler;

import cool.structures.Scope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

public class ClassInheritancePassVisitor implements ASTVisitor<Void> {
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
		String undefinedParentError = "Class %s has undefined parent %s";
		
		if (classs.error) {
			return null;
		}
		var type = classs.name;
		var classSymbol = (TypeSymbol) currentScope.lookup(type.getText());
		
		if (classs.parentType != null) {
			var parent= classs.parentType.getText();
			var parentSymbol = (TypeSymbol)currentScope.lookup(parent);
			if( parentSymbol == null) {
				
				SymbolTable.error(classs.ctx, classs.parentType,(String.format(undefinedParentError, type.getText(),
		    				parent)));
				classs.error = true;
				return null;
		
			}
			classSymbol.setParrentClass(parentSymbol);
			classSymbol.setParrent(parentSymbol);
			parentSymbol.addChild(classSymbol);
		}
		else {
			var rootClass = (TypeSymbol) currentScope.lookup("Object");
			classSymbol.setParrentClass(rootClass);
			classSymbol.setParrent(rootClass);
			rootClass.addChild(classSymbol);
		}
		return null;
	}

	@Override
	public Void visit(VarDef vardef) {
		return null;
	}

	@Override
	public Void visit(FuncDef funcdef) {
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
		return null;
	}

	@Override
	public Void visit(NotL not) {
		return null;
	}

	@Override
	public Void visit(Relational rel) {
		return null;
	}

	@Override
	public Void visit(NotA not) {
		return null;
	}

	@Override
	public Void visit(PlusMinus pm) {
		return null;
	}

	@Override
	public Void visit(MultDiv md) {
		return null;
	}

	@Override
	public Void visit(IsVoid voidd) {
		return null;
	}

	@Override
	public Void visit(New type) {
		return null;
	}

	@Override
	public Void visit(Formal f) {
		return null;
	}

	@Override
	public Void visit(Definition def) {
		return null;
	}

	@Override
	public Void visit(CaseArg cs) {
		return null;
	}

	@Override
	public Void visit(Case c) {
		return null;
	}

	@Override
	public Void visit(Let let) {
		return null;
	}

	@Override
	public Void visit(Brackets br) {
		return null;
	}

	@Override
	public Void visit(If iff) {
		return null;
	}

	@Override
	public Void visit(While wh) {
		return null;
	}

	@Override
	public Void visit(Call call) {
		return null;
	}

	@Override
	public Void visit(Dispatch at) {
		return null;
	}

	@Override
	public Void visit(Assign asg) {
		return null;
	}

}
