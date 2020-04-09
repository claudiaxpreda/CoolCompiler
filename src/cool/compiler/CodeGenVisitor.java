package cool.compiler;

import java.util.LinkedList;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import cool.structures.Scope;
import cool.structures.SymbolTable;
import cool.structures.TypeSymbol;

public class CodeGenVisitor implements ASTVisitor<ST> {
	Scope currentScope = SymbolTable.globals;


	static STGroupFile templates = new STGroupFile("cgen.stg");
	ST dataSection;	
	ST methodCodeGenSt;	
	
	
	ST globalSection; 
	ST tagsSection; 
	ST constStr; 
	ST constInt; 
	ST constBool; 
	ST classNameTab; 
	ST classObjTab; 
	ST defaultProtObj; 
	ST userDefiniedProtObj; 
	ST dispTabs; 
	ST heapStart; 
	ST inits; 

	@Override
	public ST visit(Program prog) {
		var programST = templates.getInstanceOf("program");
		
		
		methodCodeGenSt = templates.getInstanceOf("sequence");
		
		
		globalSection = MipsCodeGenerator.globalSt();
		tagsSection = MipsCodeGenerator.tagsSt();

		constStr = templates.getInstanceOf("sequence");
		constInt = templates.getInstanceOf("sequence");
		constBool = MipsCodeGenerator.boolSt();

		programST.add("globals", globalSection);
		programST.add("tags", tagsSection);

		MipsCodeGenerator.addDefaultStrings(constStr, constInt);
		programST.add("strConst", constStr);
		programST.add("intConst", constInt);
		programST.add("boolConst", constBool);
				
		defaultProtObj = MipsCodeGenerator.defaultProtObj();
		programST.add("defaultProtObj", defaultProtObj);
		
		// Prots for user defined structures
		userDefiniedProtObj = templates.getInstanceOf("sequence");

		dispTabs = MipsCodeGenerator.addDispTabs();
		programST.add("dispTabs", dispTabs);

		
		heapStart = MipsCodeGenerator.defaultHeapStart();
		programST.add("heapStart", heapStart);
		
		inits = templates.getInstanceOf("sequence");
		MipsCodeGenerator.addClassInit(inits, "Object", null, null);
		MipsCodeGenerator.addClassInit(inits, "IO", "Object", null);
		MipsCodeGenerator.addClassInit(inits, "Int", "Object", null);
		MipsCodeGenerator.addClassInit(inits, "String", "Object", null);
		MipsCodeGenerator.addClassInit(inits, "Bool", "Object", null);
		
		
		for (var e : prog.classes)
			e.accept(this);
		

		programST.add("data", dataSection);
		programST.add("methodCodeGenSt", methodCodeGenSt);

		
		classNameTab = MipsCodeGenerator.defaultNameTab();
		programST.add("classNameTab", classNameTab);
		classObjTab = MipsCodeGenerator.defaultObjTab();
		programST.add("classObjTab", classObjTab);

		programST.add("userDefiniedProtObj", userDefiniedProtObj);
		programST.add("inits", inits);

		
		return programST;

	}

	@Override
	public ST visit(ClassDef classs) {
//		currentScope = (TypeSymbol) currentScope.lookup(classs.name.getText());
//		
//		CodeGenHelper.addString(constStr, classs.name.getText(), constInt);
//
//		List<ST> classVarList = new LinkedList<ST>();
//		List<ST> classFunctionList = new LinkedList<ST>();
//		for (var feature: classs.attributes) {
//			int pos = 0;
//			var visited = feature.accept(this);
//			//int pos = InheritVarsGenerator.getVarPos(Helpers.getCurrentClass(currentScope), ((FeatureVariable) feature).object.token.getText());
//			if(visited != null)
//				visited.add("e", "    sw      $a0 " + pos + "($s0)");
//			classVarList.add(visited);
//		}
//		
//		for (var feature: classs.methods) {
//			var visited = feature.accept(this);
//			classFunctionList.add(visited);
//		}
//		
//		String className = Helpers.getCurrentClass(currentScope).getName();
//		String inheritString = Helpers.getParentClass(Helpers.getCurrentClass(currentScope)).getName();
//		CodeGenHelper.addClassInit(inits, className, inheritString, classVarList);
//
//		
//		// <class>_protObj Prototype Objects for 
//		CodeGenHelper.addProtObj(userDefiniedProtObj, classs.name.getText());
//
//		for (var methodST : classFunctionList)
//			methodCodeGenSt.add("e", methodST);
//
//		currentScope = currentScope.getParent();
		return null;
	}

	@Override
	public ST visit(VarDef vardef) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(FuncDef funcdef) {
		//var st = templates.getInstanceOf("methodGen");
//		st.add("className", ((TypeSymbol)currentScope).getName());
//		st.add("methodName", funcdef.name.getText());
//		st.add("e", funcdef.body.accept(this));
//		
//		if(funcdef.formals != null)
//			st.add("paramsClear", "    addiu   $sp $sp "+ (4 + 4 * funcdef.formals.size()) +"    # params free");
//		return st;
		return null;
	}

	@Override
	public ST visit(Id id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Int intt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(TrueVal truee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(FalseVal falsee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(StringC string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Paren paren) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(NotL not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Relational rel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(NotA not) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(PlusMinus pm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(MultDiv md) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(IsVoid voidd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(New type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Formal f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Definition def) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(CaseArg cs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Case c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Let let) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Brackets br) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(If iff) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(While wh) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Call call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Dispatch at) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ST visit(Assign asg) {
		// TODO Auto-generated method stub
		return null;
	}

}
