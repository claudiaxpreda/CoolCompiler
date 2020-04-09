package cool.compiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import cool.structures.FunctionSymbol;
import cool.structures.Scope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

public class MipsCodeGenerator {
	static STGroupFile templates = new STGroupFile("cgen.stg");
	
	static int uniqueIDString = -1;
	public static int getUniqueIDString() {
		uniqueIDString++;
		return uniqueIDString;
	}
	
	public static ST globalSt() {
		var st = templates.getInstanceOf("sequence");
    	st.add("e", "    .align  2");
    	st.add("e", "    .globl  class_nameTab");
    	st.add("e", "    .globl  Int_protObj");
    	st.add("e", "    .globl  String_protObj");
    	st.add("e", "    .globl  bool_const0");
    	st.add("e", "    .globl  bool_const1");
    	st.add("e", "    .globl  _int_tag");
    	st.add("e", "    .globl  _string_tag");
    	st.add("e", "    .globl  _bool_tag");
    	return st;	
	}
	
	public static ST tagsSt() {
		var st = templates.getInstanceOf("sequence");
    	st.add("e", "_int_tag:");
    	st.add("e", "    .word   " + SymbolTable.INT.getTag());
    	st.add("e", "_string_tag:");
    	st.add("e", "    .word   " + SymbolTable.STRING.getTag());
    	st.add("e", "_bool_tag:");
    	st.add("e", "    .word   " + SymbolTable.BOOL.getTag());
	    return st;	

	}
	
	
	static Map<String, String> knownStringsMap = new HashMap<>() {
		public String get(Object key) {
			key = ((String) key).replace("\n", "\\n");
			key = ((String) key).replace("\t", "\\t");
			key = ((String) key).replace("\b", "\\b");
			key = ((String) key).replace("\f", "\\f");
			return super.get(key);
		};
	};
	public static void addString(ST constStr, String val, ST constInt) {
		// str_const* already exists
		if (knownStringsMap.containsKey(val))
			return;
		
		// define, if necessary, len
		if(!knownIntsMap.containsKey(val.length()))
			addInt(constInt, val.length());
		
		var st = templates.getInstanceOf("strConst");
		val = val.replace("\n", "\\n");
		val = val.replace("\t", "\\t");
		val = val.replace("\b", "\\b");
		val = val.replace("\f", "\\f");

		st.add("val", val);
		st.add("size", 5 + val.length()/4);
		st.add("id", MipsCodeGenerator.getUniqueIDString());
		st.add("intConst", knownIntsMap.get(val.length()));
		st.add("tag", SymbolTable.STRING.getTag());
		constStr.add("e", st);
		knownStringsMap.put(val, "str_const" + uniqueIDString);
	}
	
	public static void addDefaultStrings(ST constStr, ST constInt) {
		addString(constStr, "", constInt);
		addString(constStr, "Object", constInt);
		addString(constStr, "IO", constInt);
		addString(constStr, "Int", constInt);
		addString(constStr, "String", constInt);
		addString(constStr, "Bool", constInt);
	}
	
	
	
	
	static int uniqueIDInt = -1;
	public static int getUniqueIDInt() {
		uniqueIDInt++;
		return uniqueIDInt;
	}
	
	static Map<Integer, String> knownIntsMap = new HashMap<>();
	public static void addInt(ST constInt, int val) {
		if (knownIntsMap.containsKey(val))
			return;
		
		var st = templates.getInstanceOf("intConst");
		st.add("val", val);
		st.add("id", MipsCodeGenerator.getUniqueIDInt());
		st.add("tag", SymbolTable.INT.getTag());
		constInt.add("e", st);
		knownIntsMap.put(val, "int_const" + uniqueIDInt);
	}

	public static ST boolSt() {
		var st = templates.getInstanceOf("sequence");
    	st.add("e", "bool_const0:");
    	st.add("e", "    .word   " + SymbolTable.BOOL.getTag());
    	st.add("e", "    .word   4");
    	st.add("e", "    .word   Bool_dispTab");
    	st.add("e", "    .word   0");
    	st.add("e", "bool_const1:");
    	st.add("e", "    .word   " +  SymbolTable.BOOL.getTag());
    	st.add("e", "    .word   4");
    	st.add("e", "    .word   Bool_dispTab");
    	st.add("e", "    .word   1");
	    return st;
	}

	public static ST defaultNameTab() {
		var st = templates.getInstanceOf("sequence");
		st.add("e", "class_nameTab:");
		for(var cls: TagGenerator.tagsTypeSymbols)
			st.add("e", "    .word   " + knownStringsMap.get(cls.getName()));
    	
		return st;
	}
	
	public static ST defaultObjTab() {
		var st = templates.getInstanceOf("sequence");
		st.add("e", "class_objTab:");
		for(var cls: TagGenerator.tagsTypeSymbols) {
			st.add("e", "    .word   " + cls.getName() + "_protObj");
			st.add("e", "    .word   " + cls.getName() + "_init");
		}
    	return st;
	}
	
	/*
	 * .word 0
	.text
	.globl  Int_init
    .globl  String_init
    .globl  Bool_init
    .globl  Main_init
    .globl  Main.main
	 */

	public static ST defaultHeapStart() {
		var st = templates.getInstanceOf("sequence");
		st.add("e", "heap_start:");
    	st.add("e", "    .word   0");
    	st.add("e", "    .text");
    	st.add("e", "    .globl  Int_init");
    	st.add("e", "    .globl  String_init");
    	st.add("e", "    .globl  Bool_init");
    	st.add("e", "    .globl  Main_init");
    	st.add("e", "    .globl  Main.main");
 	
    	return st;
	}
	
	public static void addProtObj(ST st, String className) {
		//r varNameSymbol = InheritVarsGenerator.inheritVars.get(className);
		int size = 3;
		st.add("e", className + "_protObj:");
    	st.add("e", "    .word   " + ((TypeSymbol) SymbolTable.globals.lookup(className)).getTag());
    	st.add("e", "    .word   " + size);
    	st.add("e", "    .word   " + className + "_dispTab");
    	
//    	
//    	for(var symbol : varNameSymbol.values()) {
//    		TypeSymbol typeSymbol = ((ObjectSymbol) symbol).getType();
//	    	if(typeSymbol.getName().equals("Int"))
//				st.add("e", "    .word   " + CodeGenHelper.knownIntsMap.get(0));
//			else if(typeSymbol.getName().equals("String"))
//				st.add("e", "    .word   " + CodeGenHelper.knownStringsMap.get(""));
//			else if(typeSymbol.getName().equals("Bool"))
//				st.add("e", "    .word   " + "bool_const0");
//			else st.add("e", "    .word   " + "0");
//    	}
	}
	
	public static ST defaultProtObj() {
		var st = templates.getInstanceOf("sequence");
		st.add("e", "Object_protObj:");
    	st.add("e", "    .word   " + SymbolTable.OBJECT.getTag());
    	st.add("e", "    .word   3");
    	st.add("e", "    .word   Object_dispTab");
    	
    	st.add("e", "IO_protObj:");
    	st.add("e", "    .word   " + SymbolTable.IO.getTag());
    	st.add("e", "    .word   3");
    	st.add("e", "    .word   IO_dispTab");

    	st.add("e", "Int_protObj:");
    	st.add("e", "    .word   " + SymbolTable.INT.getTag());
    	st.add("e", "    .word   4");
    	st.add("e", "    .word   Int_dispTab");
    	st.add("e", "    .word   0");

    	st.add("e", "String_protObj:");
    	st.add("e", "    .word   " + SymbolTable.STRING.getTag());
    	st.add("e", "    .word   5");
    	st.add("e", "    .word   String_dispTab");
    	st.add("e", "    .word   " + knownIntsMap.get(0));
    	st.add("e", "    .asciiz \"\"");
    	st.add("e", "    .align  2");

		st.add("e", "Bool_protObj:");
    	st.add("e", "    .word   " + SymbolTable.BOOL.getTag());
    	st.add("e", "    .word   4");
    	st.add("e", "    .word   Bool_dispTab");
    	st.add("e", "    .word   0");

    	
    	return st;
	}

	public static void addClassInit(ST st, String className, String parentClassName, List<ST> otherInits) {
		st.add("e", className + "_init:");
    	st.add("e", "    addiu   $sp $sp -12");
    	st.add("e", "    sw      $fp 12($sp)");
    	st.add("e", "    sw      $s0 8($sp)");
    	st.add("e", "    sw      $ra 4($sp)");
    	st.add("e", "    addiu   $fp $sp 4");
    	st.add("e", "    move    $s0 $a0");
    	if(parentClassName != null)
        	st.add("e", "    jal     " + parentClassName + "_init");
    	
    	// Other inits
    	st.add("e", otherInits);
    	
    	st.add("e", "    move    $a0 $s0");
    	st.add("e", "    lw      $fp 12($sp)");
    	st.add("e", "    lw      $s0 8($sp)");
    	st.add("e", "    lw      $ra 4($sp)");
    	st.add("e", "    addiu   $sp $sp 12");
    	st.add("e", "    jr      $ra");
	}

	public static ST addDispTabs() {
//		var st = templates.getInstanceOf("sequence");
//		for(String className : DispatchGenerator.dispatchTabs.keySet()) {
//			st.add("e", className + "_dispTab:");
//			var value = DispatchGenerator.dispatchTabs.get(className);
//			for(String functionNameString : value.keySet()) {
//				var valueSymbol = DispatchGenerator.dispatchTabs.get(className).get(functionNameString);
//				var fClassName = (((TypeSymbol) ((FunctionSymbol) valueSymbol).getParent()).getName());
//				st.add("e", "    .word   " + fClassName + "." + functionNameString);
//			}
//		}
		
		return null;
	}
	
	
	public static ST arithmeticHelper(String opString, ST s1, ST s2) {
		var st = templates.getInstanceOf("sequence");
		st.add("e", s1);
		st.add("e", "    sw      $a0 0($sp)");
		st.add("e", "    addiu   $sp $sp -4");
		st.add("e", s2);
		st.add("e", "    jal     Object.copy");
		st.add("e", "    lw      $t1 4($sp)");
		st.add("e", "    addiu   $sp $sp 4");
		st.add("e", "    lw      $t1 12($t1)     # int slot");
		st.add("e", "    lw      $t2 12($a0)     # int slot");
		st.add("e", "    " + opString + "     $t1 $t1 $t2");
		st.add("e", "    sw      $t1 12($a0)     # int slot");

		return st;
	}
	
	
	
}

