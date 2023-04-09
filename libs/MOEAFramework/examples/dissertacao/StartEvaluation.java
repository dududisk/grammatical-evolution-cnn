//REF : https://gist.github.com/dhadka

package dissertacao;

import java.io.IOException;
import java.util.Properties;

import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.AlgorithmFactory;
import dissertacao.classes.Output;
import dissertacao.parameters.EvaluationInfo; 
import dissertacao.parameters.Parameters;
import dissertacao.problem.ProposalProblem;


public class StartEvaluation {		
	
	public static void main(String[] args) throws IOException {
		
		EvaluationInfo info = new EvaluationInfo();		
		Properties properties = new Parameters().getProperties();		

		//PERCORRE OS DATASETS
		for (String datasetName : info.datesets) {

			ProposalProblem problem = new ProposalProblem(250, datasetName, info.grammar_model); //250		

			//PARA CADA DATASET PERCORRE OS ALGORITIMOS
			for (String algorithmName : info.algorithms) {	
				//GERA OS ARQUIVOS
				String file_name = datasetName+"_"+info.grammar_model+"_"+info.generations+"_"+algorithmName;							
				Output.prepare(info.path_evaluation, file_name);
				Output.write(info.path_evaluation,file_name, "dataset,algorithm,accuracy,f1score");

				int currentSeed = 1;
				while (currentSeed <= info.seeds) {

					Algorithm algorithm = AlgorithmFactory
							.getInstance()
							.getAlgorithm(algorithmName, properties, problem);						
				
					int currentGen = 1;	
					//generations é a quantidade de Gerações que o algoritmo vai evoluir
					while (currentGen <= info.generations) {
						algorithm.step();
						
						NondominatedPopulation result = algorithm.getResult();
						for (Solution solution : result) {
							if(currentGen == info.generations){
								String text = datasetName+","+algorithmName+","+(solution.getObjective(0)-1) +","+ (solution.getObjective(1)-1);
								Output.write(info.path_evaluation,file_name, text);
							}							
						}				
						currentGen++;
					}
					currentSeed++;	
				}						
			}	

		}	

		//PERCORRE OS DATASETS
		// for (String datasetName : info.datesets) {
		// 	Statistic.average(datasetName);
		// }		

		//plot.show();
		//analyzer.printAnalysis();
	}

}