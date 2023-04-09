//REF : https://gist.github.com/dhadka

package dissertacao;

import java.io.IOException;

import dissertacao.classes.Statistic;
import dissertacao.parameters.EvaluationInfo;



public class DissertacaoStatistic {
		
	
	public static void main(String[] args) throws IOException {

		EvaluationInfo info = new EvaluationInfo();	
		
		//PERCORRE OS DATASETS
		for (String datasetName : info.datesets) {
			Statistic.average(datasetName);
		}
	}

}