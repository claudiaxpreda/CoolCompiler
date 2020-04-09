package cool.compiler;

import cool.structures.*;

public class ClassDefinitionPassVisitor implements ASTVisitor<Void> {
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
		String selfTypeError = "Class has illegal name SELF_TYPE";
		String redefinedError = "Class %s is redefined";
		String illegalParentError = "Class %s has illegal parent %s";
		
		var type = classs.name;
	    var classSymbol = new TypeSymbol(type.getText(), null, currentScope);


		if (type.getText().equals("SELF_TYPE")) {
			SymbolTable.error(classs.ctx, type, selfTypeError);
			classs.error = true;
			return null;
		}
		
        if (! currentScope.add(classSymbol)) {
        	SymbolTable.error(classs.ctx, type, String.format(redefinedError, type.getText()));
        	classs.error = true;
        	return null;
        }
        
        if (classs.parentType != null) {
        	var parent= classs.parentType.getText();
        	if(parent.equals("Int") || parent.equals("Bool") || parent.equals("String")
        			|| parent.equals("SELF_TYPE")) {
        		
            	SymbolTable.error(classs.ctx, classs.parentType,(String.format(illegalParentError,
            			type.getText(), parent)));

            	classs.error = true;
            	return null;
        	}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(Formal f) {
		return null;
	}

	@Override
	public Void visit(Definition def) {
		// TODO Auto-generated method stub
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
