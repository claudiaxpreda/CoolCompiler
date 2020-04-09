package cool.compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cool.structures.DefaultScope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

public class TagGenerator {
	
	static List<TypeSymbol> tagsTypeSymbols = new ArrayList<TypeSymbol>();
	static int uniqueTag = 0;
	public static void generateTags() {
		 for (var cls : ((DefaultScope) SymbolTable.globals).getSymbols().values()) {
			 if(((TypeSymbol) cls).getName().equals("Object")) {
				 ((TypeSymbol) cls).setTag(uniqueTag);
				 tagsTypeSymbols.add((TypeSymbol) cls);
				 uniqueTag++;
				 depthSearch((TypeSymbol) cls);
				 ((TypeSymbol) cls).setParentTag(uniqueTag - 1);
			 }
		 }
		 caseOrderByType();
	}
	
	public static void depthSearch(TypeSymbol s) {
		for (var cls : ((DefaultScope) SymbolTable.globals).getSymbols().values()) {
			if((((TypeSymbol) cls).getParent()) == s) {
				((TypeSymbol) cls).setTag(uniqueTag);
				tagsTypeSymbols.add((TypeSymbol) cls);
				uniqueTag++;
				depthSearch((TypeSymbol) cls);
				 ((TypeSymbol) cls).setParentTag(uniqueTag - 1);
			}
		}
	}
	
	
	static int uniqueLabelID = 0;
	public static int getUniqueLabelID(){
		uniqueLabelID++;
		return uniqueLabelID - 1;
	}
	
	static List<String> orderListTagsClasses = new LinkedList<String>();

	public static void caseOrderByType(){
		Boolean found = true;
		String rememberClassString = null;
		while(found) {
			found = false;
			int minInterval = Integer.MAX_VALUE;
			for(var s : tagsTypeSymbols) {
				if(orderListTagsClasses.contains(s.getName()))
					continue;
				if(s.getParentTag() - s.getTag() < minInterval) {
					minInterval = s.getParentTag() - s.getTag();
					rememberClassString = s.getName();
					found = true;
				}
			}
			if(found == true)
				orderListTagsClasses.add(rememberClassString);			
		}		
	}
}
