package cool.structures;

import java.io.File;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

public class SymbolTable {
    public static Scope globals;
    
    private static boolean semanticErrors;
    
    public static TypeSymbol SELF_TYPE;
    public static TypeSymbol OBJECT;
    public static TypeSymbol IO;
    public static TypeSymbol INT;
    public static TypeSymbol STRING;
    public static TypeSymbol BOOL;
    
    /*
     * Define COOL basic types and methods and
     * add them to the global scope;
     */
    public static void defineBasicClasses() {
        globals = new DefaultScope(null);
        semanticErrors = false;
        
        //Object Class
        var objectClass = new TypeSymbol("Object", null, globals);
        globals.add(objectClass);
        OBJECT = objectClass;
        
        //Object methods
        var abortFunc= new FunctionSymbol("abort", objectClass);
        objectClass.add(abortFunc);
        
        var typeNameFunc = new FunctionSymbol("type_name", objectClass);
        objectClass.add(typeNameFunc);
        
        var copyFunc = new FunctionSymbol("copy", objectClass);
        objectClass.add(copyFunc);
        
        
        //IO Class
        var ioClass = new TypeSymbol("IO", objectClass, objectClass);
        globals.add(ioClass);
        IO = ioClass;
        objectClass.addChild(ioClass);
        
        //IO methods
        var outStringFunc = new FunctionSymbol("out_string", ioClass);
        ioClass.add(outStringFunc);

        var outIntFunc = new FunctionSymbol("out_int", ioClass);
        ioClass.add(outIntFunc);

        var inStringFunc = new FunctionSymbol("in_string", ioClass);
        ioClass.add(inStringFunc);

        var inIntFunc = new FunctionSymbol("in_int", ioClass);
        ioClass.add(inIntFunc);

        //Int Class
        var intClass = new TypeSymbol("Int", objectClass,  objectClass);
        globals.add(intClass);
        objectClass.addChild(intClass);
        INT = intClass;
        
        //Bool Class
        var boolClass = new TypeSymbol("Bool", objectClass, objectClass);
        globals.add(boolClass);
        objectClass.addChild(boolClass);
        BOOL = boolClass;
        
        //String Class
        var stringClass = new TypeSymbol("String", objectClass, objectClass);
        globals.add(stringClass);
        objectClass.addChild(stringClass);
        STRING = stringClass;
        
        //String methods
        var lengthFunc = new FunctionSymbol("length", stringClass);
        stringClass.add(lengthFunc);

        var concatFunc = new FunctionSymbol("concat", stringClass);
        stringClass.add(concatFunc);

        var substrFunc = new FunctionSymbol("substr", stringClass);
        stringClass.add(substrFunc);

        var classSelfType = new TypeSymbol("SELF_TYPE", null, objectClass);
        globals.add(classSelfType);
        SELF_TYPE = classSelfType;
        
        //Add formals and types
        abortFunc.setType(objectClass);
        typeNameFunc.setType(stringClass);
        copyFunc.setType(classSelfType);
        
        var outStringX = new IdSymbol("x");
        outStringX.setType(stringClass);
        outStringFunc.add(outStringX);
        outStringFunc.setType(classSelfType);
        
        var outIntX = new IdSymbol("x");
        outIntX.setType(intClass);
        outIntFunc.add(outIntX);
        outIntFunc.setType(classSelfType);
        
        inStringFunc.setType(stringClass);
        inIntFunc.setType(intClass);
        
        lengthFunc.setType(intClass);
        
        var concatStringS = new IdSymbol("s");
        concatStringS.setType(stringClass);
        concatFunc.add(concatStringS);
        concatFunc.setType(stringClass);
        
        var substrIntI = new IdSymbol("i");
        substrIntI.setType(intClass);
        substrFunc.add(substrIntI);
        
        var substrIntL = new IdSymbol("l");
        substrIntL.setType(intClass);
        substrFunc.add(substrIntL);
   
        substrFunc.setType(stringClass);  
    }
    
    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
