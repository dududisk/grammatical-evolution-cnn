//REF : https://gist.github.com/dhadka

package dissertacao.examples;

import java.io.IOException;
import java.util.HashMap; 
import java.util.Map;
import java.util.Properties;
 
import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.core.NondominatedPopulation; 

import dissertacao.parameters.Parameters;
import dissertacao.problem.ProposalProblem;



public class ExampleInstrumenter {
	
	
	public static void main(String[] args) throws IOException {
				
		String dateset = "octmnist";
		String grammar_model = "cec2";
		ProposalProblem problem = new ProposalProblem(250, dateset, grammar_model); //250
		Properties properties = new Parameters().getProperties();
		String[] algorithms = { "NSGAII", "SPEA2" };
		
		
		Instrumenter instrumenter = new Instrumenter();
			instrumenter.withProblem(problem);
			instrumenter.attachApproximationSetCollector();
			instrumenter.attachElapsedTimeCollector();

		Executor executor = new Executor()
			.withProblem(problem)			
		 	.withProperties(properties)
			.withInstrumenter(instrumenter);
				
		// // Store the data and compute the reference set
		 Map<String, Accumulator> results = new HashMap<String, Accumulator>();
		 NondominatedPopulation referenceSet = new NondominatedPopulation();
	
		for (String algorithm : algorithms) {
			referenceSet.addAll(executor.withAlgorithm(algorithm).run());
			results.put(algorithm, instrumenter.getLastAccumulator());			
		}
		

		// Calculate the performance metrics using the reference set
		// QualityIndicator qi = new QualityIndicator(problem, referenceSet);
		
		// for (String algorithm : algorithms) {
		// 	Accumulator accumulator = results.get(algorithm);
						
		// 	for (int i = 0; i < accumulator.size("NFE"); i++) {
		// 		List<Solution> approximationSet = (List<Solution>)accumulator.get("Approximation Set", i);
		// 		qi.calculate(new NondominatedPopulation(approximationSet));				
		// 		System.out.print("    ");
		// 		System.out.print(accumulator.get("NFE", i));
		// 		System.out.print(" ");
		// 		System.out.print(qi.getHypervolume());
		// 		System.out.println();
		// 	}
			
		// 	System.out.println();
		// }

		
		
			
	}

}