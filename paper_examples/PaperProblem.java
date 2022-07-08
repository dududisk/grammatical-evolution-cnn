package paper;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Grammar;
import org.moeaframework.problem.AbstractProblem;
import org.moeaframework.util.grammar.ContextFreeGrammar;
import org.moeaframework.util.grammar.Parser;

public class PaperProblem extends AbstractProblem {
	
	private static final String bnf = 
		  "<cnn>     ::= <blocks> <fc> '*lr-' <lr> \n"
	    + "<blocks>  ::= ( <block> '*' <m>) \n"
		+ "<block>   ::= ( <conv> <pool> ) \n"
		+ "<fc>      ::= 'fc*' <k> '*' <units> \n"
		+ "<pool>    ::= 'pool-' <dropout> | '' \n"
		+ "<conv>    ::= ( 'conv*' <n>) <bnorm> \n"
		+ "<dropout> ::= 'dropout' | '' \n"
		+ "<bnorm>   ::= 'bnorm-' | '' \n"
		+ "<lr>      ::= '0.1' | '0.01' | '0.001' | '0.0001' \n"
		+ "<units>   ::= '64' | '128' | '256' | '512' \n"
		+ "<k>       ::= '0' | '1' | '2' \n"
		+ "<m>       ::= '1' | '2' | '3' \n"
		+ "<n>       ::= '1' | '2' | '3'";
	
	private ContextFreeGrammar grammar;
	private int codonSize;
	private Csv csv;
	
	public PaperProblem(int codonSize) {
		super(1, 2);
		try {
			this.grammar = Parser.load(new StringReader(bnf));
			this.csv = new Csv("/home/cleber/Downloads/phenotypes_cifar10.csv");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.codonSize = codonSize;
	}

	public void evaluate(Solution solution) {
		String phenotype = this.getIndividualRepresentation(solution);
		Map<String, Double> metrics = this.csv.getMetrics(phenotype);
		if (metrics == null) {
			solution.setObjective(0, 0);
			solution.setObjective(1, 0);
			return;
		}
		double accuracy = metrics.get("accuracy");
		double f1score = metrics.get("f1_score");
		System.out.printf("%s\t\t%f\t%f\n", phenotype, accuracy, f1score);
		solution.setObjective(0, 1 - accuracy);
		solution.setObjective(1, 1 - f1score);
	}

	public Solution newSolution() {
		Solution solution = new Solution(this.numberOfVariables, this.numberOfObjectives);
		solution.setVariable(0, new Grammar(this.codonSize));
		return solution;
	}
	
	public String getIndividualRepresentation(Solution solution) {
		int[] codon = ((Grammar) solution.getVariable(0)).toArray();
		return this.grammar.build(codon);
	}
}