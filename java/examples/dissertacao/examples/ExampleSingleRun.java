//REF : https://gist.github.com/dhadka

package dissertacao.examples;

import java.io.IOException;
import java.util.Properties;

import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import dissertacao.parameters.Parameters;
import dissertacao.problem.ProposalProblem;


public class ExampleSingleRun {
	
	
	public static void main(String[] args) throws IOException {
				
		String dateset = "octmnist";
		String grammar_model = "cec2";
		ProposalProblem problem = new ProposalProblem(250, dateset, grammar_model); //250
		Properties properties = new Parameters().getProperties();
		String algorithm = "NSGAII";

		//Parameters
		//https://www.hindawi.com/journals/cin/2018/5865168/tab2/

		//########################################################
		//SINGLE RUN
		//########################################################

		NondominatedPopulation result = new Executor()
		 	.withProblem(problem)
		 	.withAlgorithm(algorithm)
		 	.withProperties(properties)
		 	//.distributeOnAllCores()
			.run();	

		System.out.format(algorithm+"%n");
		System.out.format("Accuracy  F1-Score%n");
		
		for (Solution solution : result) {
			System.out.format("%.5f\t%.5f%n", solution.getObjective(0), solution.getObjective(1));
		}

		Plot plot = new Plot();
		plot.add(algorithm, result);	
		plot.setXLabel("Accuracy");
		plot.setYLabel("F1-Score");
		plot.show();				

	}

}