package dissertacao.problem;


import java.io.IOException;
import java.io.StringReader;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Grammar;
import dissertacao.classes.Metric;
import org.moeaframework.problem.AbstractProblem;
import org.moeaframework.util.grammar.ContextFreeGrammar;
import org.moeaframework.util.grammar.Parser;

import dissertacao.classes.MetricService;
import dissertacao.classes.MetricDAO;
import dissertacao.grammar.FreeGrammar;

public class ProposalProblem extends AbstractProblem { 

	private static final String bnf = new FreeGrammar().getGrammar();

    private ContextFreeGrammar grammar;
	private int 	codonSize;
	private String 	grammar_model;
	private String	dataset; //octmnist pathmnist kmnist
		


	public ProposalProblem(int codonSize, String dataset, String grammar_model ) throws IOException {
		super(1, 2);

		try {
			this.grammar = Parser.load(new StringReader(bnf));			

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.codonSize = codonSize;
		this.dataset = dataset;
		this.grammar_model = grammar_model;

		
		// System.out.println("\n [STATUS] Iniciando Evaluate para "+dataset+" com "+grammar+"");
	}

	
	public ContextFreeGrammar getGrammar() {
		return grammar;
	}
	
    @Override
	public void evaluate(Solution solution) {	

		String grammar;	
		String dataset;	
		String phenotype;	
		double accuracy;	
		double f1_score;
		double accuracy_sd;
		double f1_score_sd;

		// MetricService metric_service = new MetricService();
		MetricDAO metric_service = new MetricDAO();

		phenotype = this.getIndividualRepresentation(solution);
		grammar = this.grammar_model;
		dataset = this.dataset;
			

		if (phenotype != null) {
			System.out.println("\n[......] Verificando "+phenotype+" em "+dataset+" com "+grammar);
			Metric metrica = metric_service.getMetric(grammar, dataset, phenotype);		
			
			if(metrica.getPhenotype() == null){
				System.out.println("[STATUS] Metrica para "+phenotype+" não disponivel.");			
				
				System.out.println("\n[......] Adicionando Phenotype na fila para Treino");
				metric_service.setMetric(grammar, dataset, phenotype, "0", "0", "0", "0", "waiting");
				System.out.println("[STATUS] "+phenotype+" adicionado na fila para Treino\n");				
				
				Metric novoStatus = metric_service.getMetric(grammar, dataset, phenotype);				
			
				while (!novoStatus.getStatus().equals("trained")) {	
					System.out.println("[......] Aguardando Metrica de "+phenotype);
					try { 
						long numMillisecondsToSleep = 10000; // 10 seconds 
						Thread.sleep(numMillisecondsToSleep); 
					} catch (InterruptedException e) {  } 
					
					novoStatus = metric_service.getMetric(grammar, dataset, phenotype);								
				}

				grammar = novoStatus.getGrammar();	
				dataset = novoStatus.getDataset();
				phenotype = novoStatus.getPhenotype();	
				accuracy = novoStatus.getAccuracy();	
				f1_score = novoStatus.getF1Score();
				accuracy_sd = novoStatus.getAccuracySD();	
				f1_score_sd = novoStatus.getF1ScoreSD();

			}else if(!metrica.getStatus().equals("trained")){

				Metric novoStatus = metric_service.getMetric(grammar, dataset, phenotype);	
				
				while (novoStatus.getStatus().equals("waiting") && novoStatus.getStatus().equals("training")) {	

					System.out.println("[......] Aguardando Metrica de "+phenotype);

					try { 
						long numMillisecondsToSleep = 10000; // 10 seconds 
						Thread.sleep(numMillisecondsToSleep); 
					} catch (InterruptedException e) {  } 
					
					novoStatus = metric_service.getMetric(grammar, dataset, phenotype);								
				}

				grammar = novoStatus.getGrammar();	
				dataset = novoStatus.getDataset();
				phenotype = novoStatus.getPhenotype();	
				accuracy = novoStatus.getAccuracy();	
				f1_score = novoStatus.getF1Score();
				accuracy_sd = novoStatus.getAccuracySD();	
				f1_score_sd = novoStatus.getF1ScoreSD();
				
			}else{
				grammar = metrica.getGrammar();	
				dataset = metrica.getDataset();
				phenotype = metrica.getPhenotype();	
				accuracy = metrica.getAccuracy();	
				f1_score = metrica.getF1Score();
				accuracy_sd = metrica.getAccuracySD();	
				f1_score_sd = metrica.getF1ScoreSD();
			}
		
			System.out.println("[RESULT] "+grammar+" | "+dataset+" | "+phenotype+" | "+accuracy+" | "+f1_score);
			solution.setObjective(0, 1 - accuracy);
			solution.setObjective(1, 1 - f1_score);
			
		}		
	}

	
	public Solution newSolution() {
		Solution solution = new Solution(this.numberOfVariables, this.numberOfObjectives);
		solution.setVariable(0, new Grammar(this.codonSize));
		return solution;
	}
	
	public String getIndividualRepresentation(Solution solution) {
		int[] codon = ((Grammar) solution.getVariable(0)).toArray();
		return this.grammar.build(codon);
	}

    
}
