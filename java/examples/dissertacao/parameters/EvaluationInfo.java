//REF : https://gist.github.com/dhadka

package dissertacao.parameters;

import java.io.IOException;
import java.util.Properties;

import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.AlgorithmFactory;
import dissertacao.classes.Output;
import dissertacao.parameters.Parameters;
import dissertacao.problem.ProposalProblem;


public class EvaluationInfo {		 
	public String[]	datesets		= {"pathmnist", "octmnist", "organmnist_axial" }; //"pathmnist", "octmnist", "organmnist_axial"
	public String[] algorithms 		= { "NSGAII",  "PESA2", "SPEA2", "DBEA",  "SMSEMOA", "eMOEA", "PAES" };   //"NSGAII",  "PESA2", "SPEA2", "DBEA",  "SMSEMOA", "eMOEA", "PAES"
	public String 	grammar_model 	= "cec21";
	public int 		seeds 			= 10; //10
	public int 		generations		= 25; //30	

	public String path_evaluation 	= "output/evaluation/";
	public String path_analyze 		= "output/analyze/";
		
}