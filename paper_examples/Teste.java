package paper;

import java.util.Properties;

import org.moeaframework.Analyzer;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.AlgorithmFactory;

public class Teste {
	

	public static void main(String[] args) throws Exception {
		
		
		PaperProblem problem = new PaperProblem(250);
		
		Properties properties = new Properties();
        properties.setProperty("populationSize", "50");
        properties.setProperty("gx.rate", "0.75");
        properties.setProperty("gm.rate", "0.05");
		
		Algorithm algorithm = AlgorithmFactory.getInstance().getAlgorithm("NSGAII", properties, problem);
		
		Analyzer analyzer = new Analyzer()
				.withProblem(problem)
				.includeAllMetrics()
				.showStatisticalSignificance();
		
		int currentGen = 1;
		
		while (currentGen <= 30) {
			algorithm.step();	
			analyzer.add("NSGAII", algorithm.getResult());
			currentGen++;
		}
		
		NondominatedPopulation result = algorithm.getResult();
		
//		analyzer.printAnalysis();
		
//		NondominatedPopulation result = new Executor()
//				.withProblem(problem)
//				.withAlgorithm("NSGAII")
//				.withProperty("populationSize", 50)
//				.withProperty("sbx.rate", 0.75)
//				.withProperty("pm.rate", 0.05)
//				.distributeOnAllCores()
//				.run();
		
		for (Solution solution : result) {
			String representation = problem.getIndividualRepresentation(solution);
			System.out.println(representation);
		}
	}
}