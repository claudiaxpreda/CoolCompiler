package cool.structures;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TypeSymbol extends Symbol implements Scope{
	protected TypeSymbol parentClass;
	protected Scope parentScope;
	protected Map<String, Symbol> symbols = new LinkedHashMap<>();
	protected Map<String, Symbol> attributes = new LinkedHashMap<>();
	protected Map<String, Symbol> functions = new LinkedHashMap<>();
	protected ArrayList<TypeSymbol> children = new ArrayList<>();
	protected Integer tag;
	protected Integer parentTag;
	    
	public TypeSymbol(String name, TypeSymbol parentClass, Scope parentScope) {
        super(name);
        this.parentClass = parentClass;
        this.parentScope = parentScope;
    }

	@Override
	public boolean add(Symbol s) {
		if (symbols.containsKey(s.getName()))
	            return false;
		
		symbols.put(s.getName(), s);
		return true;
	}

	@Override
	public Symbol lookup(String s) {
        var sym = symbols.get(s);
        
        if (sym != null)
            return sym;
      
        if (parentScope != null)
            return parentScope.lookup(s);
        
        return null;
	}

	@Override
	public Scope getParent() {
		return parentScope;
	}
	
	public TypeSymbol getParentClass() {
		return parentClass;
	}
	
	public void setParrentClass(TypeSymbol parentClass) {
		this.parentClass = parentClass;
	}
	
	public void setParrent(Scope parentClass) {
		this.parentScope = parentClass;
	}
	public void addChild(TypeSymbol child) {
		if (!this.children.contains(child))
			this.children.add(child);
	}
	
	public ArrayList<TypeSymbol> getChildren() {
		return this.children;
	}
	
	public ArrayList<TypeSymbol>getParents() {
		var parents = new ArrayList<TypeSymbol>();
		
		var parent = parentClass;
		while(parent != null) {
			parents.add(parent);
			parent = parent.getParentClass();
		}
		return parents;
	}
	
	public void setTag(Integer tag) {
		this.tag = tag;
	}
	
	public void setParentTag(Integer tag) {
		this.parentTag = tag;
	}
	
	public Integer getTag() {
		return tag;
	}
	
	public Integer getParentTag() {
		return parentTag;
	}
}
