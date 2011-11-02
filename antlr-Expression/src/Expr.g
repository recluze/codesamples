grammar Expr;

options { 
  output = AST; 
  ASTLabelType = CommonTree; 
}

prog  :  (stat {System.out.println($stat.tree.toStringTree());} )+; 

stat
  //  emit the value of the expression   
  :  expr NEWLINE         -> expr 
  | ID '=' expr NEWLINE   -> ^('=' ID expr)
  |   NEWLINE             ->  
  ; 
  
expr returns [int value]  
  : multExpr (('+'^ | '-'^) multExpr)*
  ;

multExpr returns [int value]
  : atom ('*'^ atom)*
  ;

atom returns [int value]  
  : INT
  | ID
  |   '('! expr ')'!
  ; 

ID  : ('a'..'z' | 'A'..'Z')+; 
INT : '0'..'9'+; 
NEWLINE : '\r'? '\n'; //  to accommodate different OS standards 
WS  : (' ' | '\t' | '\n' | '\r')+ { skip(); } ; 
  




//@header{
//import java.util.HashMap; 
//}
//
//@members {
//  HashMap memory = new HashMap();
//}
//prog  :  stat+; 
//
//stat
//  //  emit the value of the expression   
//  :  expr NEWLINE  
//    {System.out.println($expr.value);}
//    // for assignments, put in memory map 
//  | ID '=' expr NEWLINE 
//    {
//      System.out.println("Assigning var: " + $ID.text + " value : " + $expr.value);
//      memory.put($ID.text , new Integer($expr.value));}
//  |   NEWLINE
//  ; 
//  
//expr returns [int value]  
//  : e=multExpr {$value = $e.value;}
//    ( '+' e=multExpr {$value += $e.value;}
//    | '-' e=multExpr {$value -= $e.value;}
//    )*
//  ;
//
//multExpr returns [int value]
//  : e = atom {$value = $e.value;} 
//    ('*' e=atom {$value *= $e.value;})*
//  ;
//
//atom returns [int value]  
//  : INT
//    // just return the value  
//    {$value = Integer.parseInt($INT.text);}
//  | ID
//    // lookup the value and return it 
//    {
//      Integer v = (Integer)memory.get($ID.text);
//      if (v!=null) $value = v.intValue(); 
//      else System.err.println("undefined variabled: " + $ID.text);  
//    }
//  |   '(' expr ')'
//      // the value is expression's value 
//      {$value = $expr.value;} 
//  ; 
//
//ID  : ('a'..'z' | 'A'..'Z')+; 
//INT : '0'..'9'+; 
//NEWLINE : '\r'? '\n'; //  to accommodate different OS standards 
//WS  : (' ' | '\t' | '\n' | '\r')+ { skip(); } ; 
//  
