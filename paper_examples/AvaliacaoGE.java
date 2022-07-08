package paper;

import java.util.Properties;

import org.moeaframework.Analyzer;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.spi.AlgorithmFactory;

public class AvaliacaoGE {

	public static void main(String[] args) {

		PaperProblem problem = new PaperProblem(250);

		Properties properties = new Properties();
		properties.setProperty("populationSize", "50");
		properties.setProperty("gx.rate", "0.75");
		properties.setProperty("gm.rate", "0.05");

		Analyzer analyzer = new Analyzer()
				.withProblem(problem)
				.includeAllMetrics()
				.showStatisticalSignificance();
		

		String[] algorithms = { "NSGAII", "PAES", "PESA2", "SPEA2", "DBEA", "SMSEMOA" };
//		String[] algorithms = { "NSGAII" };

		for (String algorithmName : algorithms) {
			
			Algorithm algorithm = AlgorithmFactory
					.getInstance()
					.getAlgorithm(algorithmName, properties, problem);
			
			Plot plot = new Plot();
			
			int currentGen = 1;

			while (currentGen <= 30) {
				algorithm.step();
				NondominatedPopulation result = algorithm.getResult();
				analyzer.add(algorithmName, result);
				plot.add(algorithmName, result);
				currentGen++;
			}
			
			plot.show();
		}

//		analyzer.printAnalysis();
		
	}
}
