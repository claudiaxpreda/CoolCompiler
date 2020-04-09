package cool.compiler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import cool.lexer.CoolLexer;
import cool.structures.FunctionSymbol;
import cool.structures.IdSymbol;
import cool.structures.Scope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

public class FeaturesResolutionPassVisitor implements ASTVisitor<TypeSymbol>  {
	protected Scope currentScope = SymbolTable.globals;
	protected Boolean hasSelfType = false;
	
	@Override
	public TypeSymbol visit(Program prog) {
        for (var cl : prog.classes) {
            cl.accept(this);
        }
        return null;
	}

	@Override
	public TypeSymbol visit(ClassDef classs) {
		if (classs.error) {
			return null;
		}
		
		var type = classs.name;
		var classSymbol = (TypeSymbol) currentScope.lookup(type.getText());
		
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
	public TypeSymbol visit(VarDef vardef) {
		String attrRedefinedParentClass = "Class %s redefines inherited attribute %s";
		
		String incompatibleTypeErr = "Type %s of initialization expression of attribute "
				+ "%s is incompatible with declared type %s";
		
		var parentClass = ((TypeSymbol) currentScope).getParentClass();
		var idSymbol = vardef.nodeId.getSymbol();
		
		if (vardef.nodeId.getSymbol()== null)
			return null;
		
		while(parentClass != null ) {
			
			var scope = parentClass;
			var attrSymbol = scope.lookup(idSymbol.getName());
			if (attrSymbol != null && attrSymbol instanceof IdSymbol) {
				
				SymbolTable.error(vardef.ctx, vardef.def.name, 
						String.format(attrRedefinedParentClass,
								((TypeSymbol)currentScope).getName(),
								vardef.def.name.getText()));
				return null;
			}
			parentClass = parentClass.getParentClass();
		}

        if (vardef.def.e != null) {
        	
           var exprType = vardef.def.e.accept(this);
           var type = vardef.nodeId.getSymbol().getType();
           
           if (exprType == null || type == null)
        	   return null;
        	
			if (type != exprType) {
				var err = checkType(type, exprType);
				
				if (err != null) {
					return exprType;
				}
				
				if (hasSelfType) {
					exprType = SymbolTable.SELF_TYPE;
				}
				
				SymbolTable.error(vardef.def.ctx, vardef.def.e.token, 
						String.format(incompatibleTypeErr, exprType.getName(),
								vardef.def.nodeId.token.getText(), type.getName()));
			}
        }
        
        return null;
	}


	@Override
	public TypeSymbol visit(FuncDef funcdef) {
		String diffFormalsNoErr = "Class %s overrides method %s with different number"
				+ " of formal parameters";
		
		String diffFormalTypeErr = "Class %s overrides method %s but changes type of "
				+ "formal parameter %s from %s to %s";
		
		String diffReturnTypeErr = "Class %s overrides method %s but changes return "
				+ "type from %s to %s";
		
		String incompatibleTypeErr = "Type %s of the body of method %s is incompatible "
				+ "with declared return type %s";
		
		if (funcdef.nodeId.getSymbol() == null) {
			return null;
		}
		
		if (funcdef.nodeId.getSymbol().getType() == null) {
			return null;
		}

		var formalsNo = funcdef.formals.size();
		var name = funcdef.name.getText();
		var returnType = funcdef.nodeId.getSymbol().getType();
		
		var scope = ((TypeSymbol)currentScope).getParentClass();
		
		while(scope != SymbolTable.OBJECT) {
			var fSymbol = scope.lookup(name);
			
			if (!(fSymbol instanceof FunctionSymbol)) {
				scope = scope.getParentClass();
				continue;
			}
			
			var symbol = (FunctionSymbol) fSymbol;

			if (symbol != null) {
				var formals = ((FunctionSymbol)symbol).getFormals();
				
				if (formals.size() != formalsNo) {
					SymbolTable.error(funcdef.ctx, funcdef.name, 
							String.format(diffFormalsNoErr,
									((TypeSymbol)currentScope).getName(),name));
					return null;
				}
				
				var formalIterator = formals.entrySet().iterator();
				var currentIterator = funcdef.formals.iterator();
				
				while(currentIterator.hasNext()) {
					var formal = formalIterator.next();
					var current = currentIterator.next();
					
					var formalType = ((IdSymbol)formal.getValue()).getType();
					var currentType = current.accept(this);
					
					if (formalType == null || currentType == null )
						continue;
					
					if (formalType != currentType) {
						SymbolTable.error(funcdef.ctx, current.type, 
								String.format(diffFormalTypeErr,
										((TypeSymbol)currentScope).getName(),name, 
										current.name.getText(),
										formalType.getName(), currentType.getName()));
						return null;
					}
				}
				
				var currentReturnType = (TypeSymbol)symbol.getType();
				
				if (currentReturnType != returnType) {
					SymbolTable.error(funcdef.ctx, funcdef.type, 
							String.format(diffReturnTypeErr,
									((TypeSymbol)currentScope).getName(), name,
									currentReturnType.getName(), returnType.getName()));
					return null;
				}
			}
			scope = scope.getParentClass();
		}
		
		var newScope =  (FunctionSymbol)funcdef.nodeId.getSymbol();
		
		currentScope = newScope;
		for (var formal : funcdef.formals) {
			formal.accept(this);
		}
		
		var exprType = funcdef.body.accept(this);
		
		if (exprType == null || returnType == null) {
			currentScope = newScope.getParent();
	   	   return null;
		}
		
		if (returnType != exprType) {
			var err = checkType(returnType, exprType);
	
			if (err != null) {
				currentScope = newScope.getParent();
				return null;
			}
			
			SymbolTable.error(funcdef.ctx, funcdef.body.token,
					String.format(incompatibleTypeErr, exprType.getName(), name, 
					returnType.getName()));
		}
		currentScope = newScope.getParent();
		
		return null;
	}

	@Override
	public TypeSymbol visit(Id id) {
		String undefinedErr = "Undefined identifier %s";
		
		var symbol = (IdSymbol)currentScope.lookup(id.token.getText());
		id.setScope(currentScope);
		
		if (symbol == null && ! id.token.getText().equals("self")) {
			SymbolTable.error(id.ctx, id.token, String.format(undefinedErr,
					id.token.getText()));
			return null;
		}
		
		if (id.token.getText().equals("self")) {
			var classSymbol = currentScope;
			while(!(classSymbol instanceof TypeSymbol) && classSymbol !=  null) {
				classSymbol = classSymbol.getParent();
			}
			
			if (classSymbol == null) {
				return null;
			}

			return (TypeSymbol)classSymbol;
		}
		
		if (symbol != null)
			return symbol.getType();
		
		return null;
	}

	@Override
	public TypeSymbol visit(Int intt) {
		return SymbolTable.INT;
	}

	@Override
	public TypeSymbol visit(TrueVal truee) {
		return SymbolTable.BOOL;
	}

	@Override
	public TypeSymbol visit(FalseVal falsee) {
		return SymbolTable.BOOL;
	}

	@Override
	public TypeSymbol visit(StringC string) {
		return SymbolTable.STRING;
	}

	@Override
	public TypeSymbol visit(Paren paren) {
		return  paren.e.accept(this);
	}

	@Override
	public TypeSymbol visit(NotL not) {
		String boolOpErr = "Operand of not has type %s instead of Bool";
		
		var BOOL = SymbolTable.BOOL;
		
		var exprType = not.e.accept(this);
	
		if (exprType != null && exprType != BOOL) {
			SymbolTable.error(not.ctx, not.e.token, String.format(boolOpErr, exprType.getName()));
			return null;
		}
		return exprType;
	}

	@Override
	public TypeSymbol visit(Relational rel) {
		checkOperands(rel.ctx, rel.op, rel.left, rel.right);
		return SymbolTable.BOOL;
	}

	@Override
	public TypeSymbol visit(NotA not) {
		String intOpErr = "Operand of %s has type %s instead of Int";
		var INT = SymbolTable.INT;
		
		var exprType = not.e.accept(this);

		if (exprType != null && exprType != INT) {
			SymbolTable.error(not.ctx, not.e.token, String.format(intOpErr,
					not.token.getText(), exprType.getName()));
			return null;
		}
		
		return exprType;	
	}

	@Override
	public TypeSymbol visit(PlusMinus pm) {
		return checkOperands(pm.ctx, pm.op, pm.left, pm.right);
	}

	@Override
	public TypeSymbol visit(MultDiv md) {
		return checkOperands(md.ctx, md.op, md.left, md.right);
	}

	@Override
	public TypeSymbol visit(IsVoid voidd) {
		voidd.e.accept(this);
		return SymbolTable.BOOL;
	}

	@Override
	public TypeSymbol visit(New type) {
		String undefinedTypeErr = "new is used with undefined type %s";
	
		var typeSymbol = (TypeSymbol)currentScope.lookup(type.type.getText());
		
		if (typeSymbol == null) {
			SymbolTable.error(type.ctx, type.type, String.format(undefinedTypeErr,
					type.type.getText()));
			return null;
		}

		return typeSymbol;
	}

	@Override
	public TypeSymbol visit(Formal f) {
		if (f.nodeId.getSymbol() != null)
			return f.nodeId.getSymbol().getType();
		
		return null;
	}

	@Override
	public TypeSymbol visit(Definition def) {
		String incompatibleTypeErr = "Type %s of initialization expression of "
				+ "identifier %s is incompatible with declared type %s";
		
		var symbol = def.nodeId.getSymbol();
		
		if (symbol == null)
			return null;
		
		if ( def.e != null) {
			var exprType =  def.e.accept(this);
			var type = (TypeSymbol)symbol.getType();
			currentScope.add(symbol);
			
			if (type == null || exprType == null)
				return null;
			
			if (type != exprType) {
				var err = checkType(type, exprType);
				
				if (err != null) {
					return exprType;
				}
				SymbolTable.error(def.ctx, def.e.token, String.format(incompatibleTypeErr, 
						exprType.getName(), def.nodeId.token.getText(), type.getName()));
				return null;
			}
			return exprType;
		}
		currentScope.add(symbol);
		return null;
	}

	@Override
	public TypeSymbol visit(CaseArg cs) {		
		return  cs.init.accept(this);
	}

	@Override
	public TypeSymbol visit(Case c) {		
		c.e.accept(this);
		LinkedHashMap<TypeSymbol, ArrayList<TypeSymbol>> parents = new LinkedHashMap<>();
		
		for (var cond : c.conds ) {
			var type = cond.accept(this);
			
			if (type == null)
				continue;
			
			var parentsType = type.getParents();
			parentsType.add(0, type);
			for (var parent : parentsType) {
				if (parents.containsKey(parent)) {
					parents.get(parent).add(type);
				}
				else {
					parents.put(parent, new ArrayList<TypeSymbol>());
					parents.get(parent).add(type);
				}
			}
		}

		var keys = parents.keySet();
		
		for (var key : keys) {			
			if (c.conds.size() == parents.get(key).size()) {
				return key;
			}
		}
		return null;
	}

	@Override
	public TypeSymbol visit(Let let) {
		var newScope = let.nodeId.getScope();
		currentScope = newScope;

		for (var def : let.defs ) {
			def.accept(this);
		}
		
		var type = let.e.accept(this);
		currentScope = newScope.getParent();
		
		return type;
	}

	@Override
	public TypeSymbol visit(Brackets br) {	
		TypeSymbol type = null;
		
		for (var e : br.body) {
			type = e.accept(this);
		}
		
		return type;
	}

	@Override
	public TypeSymbol visit(If iff) {
		String boolTypeErr = "If condition has type %s instead of Bool";
		var BOOL = SymbolTable.BOOL;
		var exprType = iff.cond.accept(this);
		
		if (exprType == null)
			return null;
		
		if (exprType != BOOL ) {
			SymbolTable.error(iff.ctx, iff.cond.token, String.format(boolTypeErr,
					exprType.getName()));
		}
		
		var thenType = iff.thenBranch.accept(this);
		var elseType = iff.elseBranch.accept(this);
		
		if (thenType == null || elseType == null)
			return null;
	
		if (thenType != elseType) {
			var thenParents = thenType.getParents();
			var parentsElse = elseType.getParents();
			
			thenParents.add(thenType);
			for (var parent : parentsElse) {
				if (thenParents.contains(parent)) {
					return parent;
				}
			}	
			
			return SymbolTable.OBJECT;
		}
		return thenType;
	}

	@Override
	public TypeSymbol visit(While wh) {
		String boolTypeErr = "While condition has type %s instead of Bool";
		var BOOL =  SymbolTable.BOOL;
		var exprType = wh.cond.accept(this);
		
		if (exprType == null)
			return null;
		
		if (exprType != BOOL ) {
			SymbolTable.error(wh.ctx, wh.cond.token, String.format(boolTypeErr,
					exprType.getName()));
		}
		
		wh.body.accept(this);
		return  SymbolTable.OBJECT;
	}

	@Override
	public TypeSymbol visit(Call call) {
		var classSymbol = currentScope;
		
		while(!(classSymbol instanceof TypeSymbol) && classSymbol !=  null) {
			classSymbol = classSymbol.getParent();
		}
		
		if (classSymbol == null) {
			return null;
		}
		
		return checkFunction(call.ctx, (TypeSymbol)classSymbol, call.name, call.args );
	}

	@Override
	public TypeSymbol visit(Dispatch at) {
		String selfTypeErr = "Type of static dispatch cannot be SELF_TYPE";
		String undefinedTypeErr = "Type %s of static dispatch is undefined";
		String superClassErr = "Type %s of static dispatch is not a superclass of type %s";

		var funcName = at.id;
		TypeSymbol returnType;
		
		if (at.e.token.getText().equals("self")) {
			var classSymbol = currentScope;
			while(!(classSymbol instanceof TypeSymbol) && classSymbol !=  null) {
				classSymbol = classSymbol.getParent();
			}
			
			if (classSymbol == null) {
				return null;
			}
			return checkFunction(at.ctx, (TypeSymbol)classSymbol, funcName, at.args );
		}

		var exprType = at.e.accept(this);
		if (exprType == null)
			return null;
		
		if (at.type != null) {
			
			if (at.type.getText().equals("SELF_TYPE"))  {
				SymbolTable.error(at.ctx, at.type, selfTypeErr);
				return null;
			}
			var typeSymbol = (TypeSymbol)currentScope.lookup(at.type.getText());
			
			if (typeSymbol == null) {
				SymbolTable.error(at.ctx, at.type, String.format(undefinedTypeErr,
						at.type.getText()));
				return null;
			}
			
			if (! exprType.getParents().contains(typeSymbol)) {
				SymbolTable.error(at.ctx, at.type, String.format(superClassErr,
						at.type.getText(), exprType.getName()));
				return null;
			}
			
			returnType = checkFunction(at.ctx, typeSymbol, funcName, at.args);
			returnType = exprType;			
		} else {
			returnType = checkFunction(at.ctx, exprType, funcName, at.args);
		}
		return returnType;
	}

	@Override
	public TypeSymbol visit(Assign asg) {
		String selfAsgErr = "Cannot assign to self";
		String incompatibleTypeErr = "Type %s of assigned expression is incompatible "
				+ "with declared type %s of identifier %s";
		
		if (asg.name.getText().equals("self")) {
			SymbolTable.error(asg.ctx, asg.name, selfAsgErr);
			return null;
		}
		
		var type = asg.nodeId.accept(this);
		var exprType = asg.e.accept(this);
		
		if (type == null || exprType == null)
			return null;
		
		if (type != exprType) {
			
			var err = checkType(type, exprType);
			
			if (err != null) {
				return exprType;
			}
			
			SymbolTable.error(asg.ctx, asg.e.token, String.format(incompatibleTypeErr,
					exprType.getName(), type.getName(), asg.nodeId.token.getText()));
			return null;
		}
		
		return exprType;
	}

	/*
	 * This function checks if arithmetics operands and relational operands
	 * have the right type;
	 */
	TypeSymbol checkOperands(ParserRuleContext ctx, Token token, Expression e1, Expression e2) {
		String intOpErr = "Operand of %s has type %s instead of Int";
		String compareOpErr = "Cannot compare %s with %s";
		
		var INT = SymbolTable.INT;
		var BOOL =  SymbolTable.BOOL;
		var STRING =  SymbolTable.STRING;
		
		var type1 = e1.accept(this);
		var type2 = e2.accept(this);
		
		if (type1 == null && type2 == null)
			return null;
		
		if (token.getType() != CoolLexer.EQ) {
			if (type1 == INT && type2 == INT) {
				return INT;
			}
			
			if (type1 != INT && type1 != null) {
				SymbolTable.error(ctx, e1.token, String.format(intOpErr,
						token.getText(), type1.getName()));
			}
			
			if (type2 != INT && type2 != null) {
				SymbolTable.error(ctx, e2.token, String.format(intOpErr,
						token.getText(), type2.getName()));
			}
		}
		else {
			if (type1 != type2) {
				if(type1 == INT || type1 == BOOL || type1 == STRING ) {
					SymbolTable.error(ctx, token, String.format(compareOpErr,
							type1.getName(), type2.getName()));
					return null;
				}
				
				if(type2 == INT || type2 == BOOL || type2 == STRING ) {
					SymbolTable.error(ctx, token, String.format(compareOpErr,
							type2.getName(), type1.getName()));
					return null;
				}
			}
		}
		
		return null;
	}
	
	/*
	 * This function checks if a method exists and can be used in the current class
	 * returning its type;
	 */
	TypeSymbol checkFunction(ParserRuleContext ctx, TypeSymbol typeSymbol, Token funcName, List<Expression> args) {
		String undefinedMethodErr = "Undefined method %s in class %s";
		String wrongParamNoErr = "Method %s of class %s is applied to wrong number of arguments";
		String wrongParamType = "In call to method %s of class %s, actual type %s of formal parameter"
				+ " %s is incompatible with declared type %s";
		var funcSymbol = typeSymbol.lookup(funcName.getText());
		
		if (funcSymbol ==  null || !(funcSymbol instanceof FunctionSymbol) ) {
			SymbolTable.error(ctx, funcName, String.format(undefinedMethodErr,
					funcName.getText(), typeSymbol.getName()));
			return null;
		}
		
		var formals = ((FunctionSymbol)funcSymbol).getFormals();
		if (args.size() != formals.size()) {
			SymbolTable.error(ctx, funcName, String.format(wrongParamNoErr,
					funcName.getText(), typeSymbol.getName()));
			return null;
		}
		
		var formalIterator = formals.entrySet().iterator();
		var currentIterator = args.iterator();
		
		while(currentIterator.hasNext()) {
			var formal = formalIterator.next();
			var current = currentIterator.next();
			
			var formalType = ((IdSymbol)formal.getValue()).getType();
			var currentType = current.accept(this);
			
			if (formalType == null || currentType == null)
				continue;
			
			if (formalType != currentType) {
				var err  = checkType(formalType, currentType);
				
				if (err == null) {
					SymbolTable.error(ctx, current.token, 
						String.format(wrongParamType, funcName.getText(), typeSymbol.getName(),
								currentType.getName(),
								formal.getValue().getName(), formalType.getName()));
				}
			}
		}
		
		var returnType = ((FunctionSymbol)funcSymbol).getType();
		
		if (returnType == SymbolTable.SELF_TYPE) {
			hasSelfType = true;
			return getCurrentClass(typeSymbol);
		} else {
			hasSelfType = false;
		}
		
		return returnType;
	}
	
	/*
	 * This function checks if two types are compatible;
	 */
	TypeSymbol checkType(TypeSymbol type1, TypeSymbol type2) {
		if (type2 == SymbolTable.SELF_TYPE) {
			type2 = getCurrentClass(currentScope);
		}
		
		if (type1 == SymbolTable.SELF_TYPE) {
			type1 = getCurrentClass(currentScope);
		}
		
		if (type1 == null || type2 == null) {
			return null;
		}
		
		if (type1 == type2) {
			return type1;
		}
		
		var childrenType = type1.getChildren();
		for (var child : childrenType) {
			if (child == type2) {
				return child;
			}
		}
		return null;
	}
	
	/*
	 * This function finds the current class
	 * of the current scope;
	 */
	TypeSymbol getCurrentClass(Scope scope) {
		var classSymbol = scope;
		
		while(!(classSymbol instanceof TypeSymbol) && classSymbol !=  null) {
			classSymbol = classSymbol.getParent();
		}
		return (TypeSymbol)classSymbol;
	}
}
