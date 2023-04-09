//REF : https://gist.github.com/dhadka

package dissertacao.grammar;

public class FreeGrammar{
	
	//http://moeaframework.org/javadoc/org/moeaframework/core/operator/StandardOperators.html
	
	public String getGrammar(){
		String bnf = 
			 "<final> ::= <exp2> <fc> *lr- <lr> \n"
			+"<exp2> ::= (<exp1> * <m>) \n"
			+"<exp1> ::= (<conv> <pool>) \n"
			+"<fc> ::= fc * <k> * <fcneurons> \n"
			+"<pool> ::= pool - <dropout> | '' \n"
			+"<conv> ::= (conv * <n>) <bnorm> \n"
			+"<dropout> ::= dropout | '' \n"
			+"<bnorm> ::= bnorm- | '' \n"
			+"<lr> ::= 0.1 | 0.01 | 0.001 | 0.0001 \n"
			+"<fcneurons> ::= 64 | 128 | 256 | 512 \n"
			+"<n> ::= 1 | 2 | 3 \n"
			+"<k> ::= 0 | 1 | 2 \n"
			+"<m> ::= 1 | 2 | 3 \n";
		
		return bnf;
	}
}
