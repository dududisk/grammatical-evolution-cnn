//REF : https://gist.github.com/dhadka

package dissertacao.parameters;

import java.util.Properties;

public class Parameters{
	
	//http://moeaframework.org/javadoc/org/moeaframework/core/operator/StandardOperators.html
	
	public Properties getProperties(){
		Properties properties = new Properties();
		
		properties.setProperty("populationSize", "50"); //50
		properties.setProperty("gx.rate", "0.75"); //Single-point crossover for grammars. A crossover point is selected in both parents with the tail portions swapped.
		properties.setProperty("gm.rate", "0.05"); //Uniform mutation for grammars. Each integer codon in the grammar representation is uniformly mutated with a specified probability.
	
		//properties.setProperty("sbx.rate", "0.75"); //Simulated binary crossover (SBX) operator. SBX attempts to simulate the offspring distribution of binary-encoded single-point crossover on real-valued decision variables. An example of this distribution, which favors offspring nearer to the two parents, is shown below.
		
		//properties.setProperty("sbx.distributionIndex", "30.0"); //Distribution index for simulated binary crossover				
		
		//properties.setProperty("pm.rate", "0.05"); //Polynomial mutation (PM) operator. PM attempts to simulate the offspring distribution of binary-encoded bit-flip mutation on real-valued decision variables. Similar to SBX, PM favors offspring nearer to the parent.
		
		//properties.setProperty("pm.distributionIndex", "30.0"); //Mutation rate for polynomial mutation
		
		//properties.setProperty("pm.withReplacement", "true"); //Binary tournament selection//
		
		//properties.setProperty("tournament", "2");	
		
		return properties;
	}
}
