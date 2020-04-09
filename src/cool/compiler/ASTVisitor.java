package cool.compiler;

public interface ASTVisitor<T> {
	T visit(Program prog);
	T visit(ClassDef classs);
	T visit(VarDef vardef);
	T visit(FuncDef funcdef);
    T visit(Id id);
    T visit(Int intt);
    T visit(TrueVal truee);
    T visit(FalseVal falsee);
    T visit(StringC string);
    T visit(Paren paren);
    T visit(NotL not);
    T visit(Relational rel);
    T visit(NotA not);
    T visit(PlusMinus pm);
    T visit(MultDiv md);
    T visit(IsVoid voidd);
    T visit(New type);
    T visit(Formal f);
    T visit(Definition def);
    T visit(CaseArg cs);
    T visit(Case c);
    T visit(Let let);
    T visit(Brackets br);
    T visit(If iff);
    T visit(While wh);
    T visit(Call call);
    T visit(Dispatch at);
    T visit(Assign asg); 
}