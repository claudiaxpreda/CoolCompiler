package cool.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import cool.structures.DefaultScope;
import cool.structures.Scope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

public class Helpers {
    public static boolean is_self_type(TypeSymbol s) {
    	if(s.getParent() == SymbolTable.SELF_TYPE)
    		return true;
    	return false;
    }
    
    public static boolean equals_self_type(TypeSymbol s) {
    	if(s.getName().equals("SELF_TYPE"))
    		return true;
    	return false;
    }
    
    public static TypeSymbol self_type_to_type(TypeSymbol s) {
    	return (TypeSymbol) SymbolTable.globals.lookup(s.getName());
    }
    
    // typ - superclass
    // asgnSymbol - subclass
    public static TypeSymbol subclass_of(TypeSymbol typ, TypeSymbol asgnSymbol) {
		
    	// not!!! T < SELF_TYPE_C
    	if(is_self_type(typ))
			return null;

		// SELF_TYPE_C < T <= C < T
    	asgnSymbol = self_type_to_type(asgnSymbol);
    	
    	
    	// ANY < Object
    	if(typ.getName().equals("Object"))
    		return typ;
    	
    	
		if(!typ.getName().equals(asgnSymbol.getName())) {
			while(asgnSymbol.getParent() != SymbolTable.globals) {
				asgnSymbol = (TypeSymbol) asgnSymbol.getParent();
				if(typ.getName().equals(asgnSymbol.getName())) {
					return typ;
				}
			}
		}
		else 
			return typ;
		return null;
				

    }
    public static TypeSymbol least_upper_bound(TypeSymbol type2, TypeSymbol type3) {
		type2 = self_type_to_type(type2);
		type3 = self_type_to_type(type3);
		
    	Scope type2_it = type2;
		Scope type3_it = type3;
		TypeSymbol ret_ifSymbol = null;

		while(type2_it != SymbolTable.globals) {
			while(type3_it != SymbolTable.globals) {
				if(((TypeSymbol) type2_it).getName().equals(((TypeSymbol)type3_it).getName())) {
					ret_ifSymbol = (TypeSymbol) type2_it;
					break;
				}
				type3_it = type3_it.getParent();
			}
			if(ret_ifSymbol != null)
				break;
			type2_it = type2_it.getParent();
			type3_it = type3;
		}
		
		if(ret_ifSymbol == null)
			ret_ifSymbol = SymbolTable.OBJECT;

		return ret_ifSymbol;
    }
    
    public static TypeSymbol aritmethicTypeCheck(Expression e, TypeSymbol s1, TypeSymbol s2, ParserRuleContext c1, Token t1, ParserRuleContext c2, Token t2, String op) {
		if(s1 != null) {
			if(!s1.getName().equals("Int")) {
				SymbolTable.error(c1, t1, "Operand of " + op + " has type " + s1.getName() + " instead of Int");
				return null;			
			}
		}
		if(s2 != null) {
			if(!s2.getName().equals("Int")) {
				SymbolTable.error(c2, t2, "Operand of " + op + " has type " + s2.getName() +  " instead of Int");
				return null;			
			}
		}
		//e.setETypeSymbol((TypeSymbol) SymbolTable.globals.lookup("Int", Scope.ID_TYPE));
		return SymbolTable.INT;
    }
    
    public static TypeSymbol getCurrentClass(Scope currentScope) {
	    Scope returnScope = currentScope;
		while(! (returnScope instanceof TypeSymbol)) {
			returnScope = returnScope.getParent();
		}
		return (TypeSymbol) returnScope;
    }
    
    public static TypeSymbol getParentClass(TypeSymbol s) {
    	if(s.getParent() instanceof DefaultScope)
    		return null;
    	return (TypeSymbol) s.getParent();
    }
}
