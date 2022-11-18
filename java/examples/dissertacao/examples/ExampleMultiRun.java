//REF : https://gist.github.com/dhadka


package dissertacao.examples;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
 
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import dissertacao.parameters.Parameters;
import dissertacao.problem.ProposalProblem;

public class ExampleMultiRun {
	
	
	public static void main(String[] args) throws IOException {
				
		String dateset = "octmnist";
		String grammar_model = "cec2";
		ProposalProblem problem = new ProposalProblem(250, dateset, grammar_model); //250
		Properties properties = new Parameters().getProperties();
		String algorithm = "NSGAII";
		int seeds = 30;

		//########################################################
		//MULTI RUNS
		//########################################################

		List<NondominatedPopulation> multiRuns = new Executor()
			.withProblem(problem)
			.withAlgorithm(algorithm)			
			.withProperties(properties)
				//.distributeOnAllCores()
			.runSeeds(seeds)
			;

			Plot plot = new Plot();
	
			double[] acc = new double[1000];
			double[] f1 = new double[1000];
			int i = 0;		
			
			
			System.out.format(algorithm+"%n");				

			for (NondominatedPopulation run : multiRuns) {	

				for (Solution solution : run) {					
					i++;
					System.out.format("%.5f\t%.5f%n", solution.getObjective(0),  solution.getObjective(1));
					//System.out.format("%.5f\t%.5f%n", solution.getObjective(0), solution.getObjective(1));

					acc[i] = solution.getObjective(0);
					f1[i] = solution.getObjective(1);
				}
				
			}

			plot.scatter(algorithm, acc, f1);
			plot.setYLabel("Accuracy");
			plot.setXLabel("F1 Score");
			plot.show();						

	}

}