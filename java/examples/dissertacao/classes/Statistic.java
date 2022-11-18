

package dissertacao.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import dissertacao.parameters.EvaluationInfo;

public class Statistic {

   
    public static void average(String dataset){        

        EvaluationInfo info = new EvaluationInfo();			
       	
        String file_name_avg    = dataset+"_"+info.grammar_model+"_"+info.generations+"_AVG";          

        //Cria o Arquivo se nao existir
        try {  
            Output.prepare(info.path_analyze, file_name_avg); 
            Output.write(info.path_analyze,file_name_avg, "ALGORITHM & ACCURACY (SD) & F1-SCORE (SD)");
        }catch (IOException ex) { 
            ex.printStackTrace(); 
        }       

        //PARA CADA DATASET PERCORRE OS ALGORITIMOS
        for (String algorithmName : info.algorithms) {	

            float TotalAccuracy= 0;
            float TotalF1Score= 0;
            float Accuracy[] = new float[20];
            float F1Score[] = new float[20];   
            double sdAccuracy= 0;
            double sdF1Score= 0;  

            String file_name_evaluation = dataset+"_"+info.grammar_model+"_"+info.generations+"_"+algorithmName;			
            File arquivo = new File(info.path_evaluation+file_name_evaluation+".csv");        
                
            try {   
                
                if (arquivo.exists()) {

                    FileReader fr = new FileReader(arquivo);
                    BufferedReader br = new BufferedReader(fr);
                    //enquanto houver mais linhas
                    int count_line = 0;
                    while (br.ready()) {
                        // le cada linha do arquivo e separa a acc e f1score e joga pra array
                        String linha = br.readLine();                    
                        String[] coluna = linha.split(",");
                        String accuracy = coluna[2];
                        String f1_score = coluna[3];                    
                    
                        if(count_line>0){
                            Accuracy[count_line] = Float.parseFloat(accuracy);
                            F1Score[count_line] = Float.parseFloat(f1_score);
                        }                     

                        ++count_line;
                    }

                    br.close();
                    fr.close();                    

                    // Calculo da Média

                    for (int i=1; i<count_line;i++){
                        TotalAccuracy=TotalAccuracy+Accuracy[i];
                        TotalF1Score=TotalF1Score+F1Score[i];         
                        
                        // Escreve no Arquivo o resultado da linha
                        // Output.write(info.path_analyze, file_name_avg, "Linha "+(i+1)+" | "+(Accuracy[i])+" | "+(i+1)+" : "+(F1Score[i]));
                    }

                    //o -1 tira da contagem o cabeçalho
                    double avgAccuracy = TotalAccuracy/(count_line-1);
                    double avgF1Score = TotalF1Score/(count_line-1);


                    //####################################################
                    // Calculo do Desvio Padrão
                    //####################################################

                    for (int i=1; i<count_line;i++){
                        sdAccuracy = sdAccuracy + Math.pow((Accuracy[i] - avgAccuracy), 2);      
                        sdF1Score = sdF1Score + Math.pow((F1Score[i] - avgF1Score), 2);            
                    }

                    //o -1 tira da contagem o cabeçalho
                    double sqAccuracy = sdAccuracy / (count_line-1);
                    double resAccuracy = Math.sqrt(sqAccuracy);

                    double sqF1Score = sdF1Score / (count_line-1);
                    double resF1Score = Math.sqrt(sqF1Score);

                    //####################################################
                    // Escreve o resultado no arquivo
                    //####################################################

                    // Output.write(info.path_analyze,file_name_avg, 
                    // "\nTotal: "+(TotalAccuracy)+" | "+(TotalF1Score)
                    // +" \nTotal Linhas: "+(count_line)
                    // +"\nAVG: "+avgAccuracy+" ("+resAccuracy+") | "+avgF1Score+" ("+resF1Score);


                    DecimalFormat df2 = new DecimalFormat("0.0000");
                    DecimalFormat df4 = new DecimalFormat("0.0000");
                    // df.setRoundingMode(RoundingMode.HALF_UP);
                   

                    
                    //OverleafFormat
                    Output.write(info.path_analyze,file_name_avg, 
                    algorithmName+" & "+df2.format(avgAccuracy)+" ($\\pm"+df4.format(resAccuracy)+"$) & "+df2.format(avgF1Score)+" ($\\pm"+df4.format(resF1Score)+"$) \\\\");

                    // Output.read(file_name_avg);

                }else{
                    System.out.println("Arquivo "+info.path_evaluation+file_name_evaluation+".csv  não encontrado");
                }
                    
            } catch (IOException ex) {
                    ex.printStackTrace();
            }

        }       

    }

    public static Double nemenyi(String file){

        return null;
    }

    public static Double friedman(String file){

        return null;
    }
    
}