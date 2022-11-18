//REF : https://gist.github.com/dhadka

package dissertacao.examples;

import java.io.IOException;
import java.util.Properties;

import org.moeaframework.util.ReferenceSetMerger;
import dissertacao.parameters.Parameters;
import dissertacao.problem.ProposalProblem;
 
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class ExampleMERGE {
	
	
	public static void main(String[] args) throws IOException {
				
		String dateset = "octmnist";
		String grammar_model = "cec2";
		ProposalProblem problem = new ProposalProblem(250, dateset, grammar_model); //250
		Properties properties = new Parameters().getProperties();

		//Parameters
		//https://www.hindawi.com/journals/cin/2018/5865168/tab2/

		//########################################################
		//EXECUTOR
		//########################################################	
		
		 
		Executor executor = new Executor()
			.withProblem(problem)			
			.withProperties(properties)
			//.distributeOnAllCores()
			;

				
		//########################################################
		//SPEA
		//########################################################			
				
		NondominatedPopulation spea = executor		
			.withAlgorithm("SPEA2")
			.run();
		
			
		//########################################################
		//NSGA
		//########################################################	
		
		NondominatedPopulation nsga = executor		
			.withAlgorithm("SPEA2")
			.run();		


		//########################################################
		//MERGE
		//########################################################

		ReferenceSetMerger merger = new ReferenceSetMerger();
			merger.add("SPEA2", spea);
			merger.add("NSGA-II", nsga);

		System.out.println("SPEA2: " + 		merger.getContributionFrom("SPEA2") + " / " 	+ spea.size());
		System.out.println("NSGA-II: " +	merger.getContributionFrom("NSGA-II") + " / " 	+ nsga.size());
	
		
		//########################################################
		//RESULT
		//########################################################

		System.out.format("SPEA%n");
		System.out.format("ACC  F1%n");
		
		for (Solution solution : spea) {
			System.out.format("%.5f\t%.5f%n", solution.getObjective(0), solution.getObjective(1));
		}

		System.out.format("NSGA-II%n");
		System.out.format("ACC  F1%n");
		for (Solution solution : nsga) {
			System.out.format("%.5f\t%.5f%n", solution.getObjective(0), solution.getObjective(1));
		}

		new Plot()
		.add("SPEA", 	spea)
		.add("NSGA-II", nsga)
		.setYLabel("Accuracy")
		.setXLabel("F1 Score")
		.show();	


	}

}