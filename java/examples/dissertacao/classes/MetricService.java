

package dissertacao.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MetricService {

    static String host = "http://mestrado-gmetrics-laravel.test/api/v1/";


    public static void setMetric(
        String grammar, 
        String dataset, 
        String phenotype, 
        String accuracy, 
        String f1_score, 
        String accuracy_sd, 
        String f1_score_sd, 
        String status) 
    {
        try{
           
            URL url = new URL(host+"metrics/");//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = 
                        URLEncoder.encode("grammar", "UTF-8") + "=" + URLEncoder.encode(grammar, "UTF-8") 
                + "&" + URLEncoder.encode("dataset", "UTF-8") + "=" + URLEncoder.encode(dataset, "UTF-8")
                + "&" + URLEncoder.encode("phenotype", "UTF-8") + "=" + URLEncoder.encode(phenotype, "UTF-8")
                + "&" + URLEncoder.encode("accuracy", "UTF-8") + "=" + URLEncoder.encode(accuracy, "UTF-8")
                + "&" + URLEncoder.encode("accuracy_sd", "UTF-8") + "=" + URLEncoder.encode(accuracy_sd, "UTF-8")
                + "&" + URLEncoder.encode("f1_score", "UTF-8") + "=" + URLEncoder.encode(f1_score, "UTF-8")
                + "&" + URLEncoder.encode("f1_score_sd", "UTF-8") + "=" + URLEncoder.encode(f1_score_sd, "UTF-8")
                + "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            bufferedReader.close();
            inputStream.close();
            conn.disconnect();

        }catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        
        
    }

    public static Metric getMetric(String grammar, String dataset, String phenotype)
    {

        // try { 
        //     long numMillisecondsToSleep = 500; // 0.5 seconds 
        //     Thread.sleep(numMillisecondsToSleep); 
        // } catch (InterruptedException e) {  } 

        Metric metric = new Metric();

        try {

            URL url = new URL(host+"metrics/search?grammar="+grammar+"&dataset="+dataset+"&phenotype="+phenotype);//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);

            String output;   
            //List<String> linhas = new ArrayList<String>();    

            while ((output = br.readLine()) != null) {

                String[] campos = output.split(",");


                for (int i = 0; i < campos.length; i++) {
                    String campo = campos[i];

                    String itemSemAspas = campo.replaceAll("[\"\\[{}\\]]","");

                    String[] registro = itemSemAspas.split(":");

                    if(registro.length == 2){
                        String titulo = registro[0];
                        String valor = registro[1];

                        //System.out.println(titulo+":"+valor);
                        //System.out.println("-------------");

                        if(titulo.equals("grammar")){                              
                            metric.setGrammar(valor);
                        }
                        if(titulo.equals("dataset")){                              
                            metric.setDataset(valor);
                        }
                        if(titulo.equals("phenotype")){ 
                            metric.setPhenotype(registro[1]); 
                        }
                        if(titulo.equals("accuracy")){                            
                            metric.setAccuracy(Float.parseFloat(registro[1])); 
                        }
                        if(titulo.equals("f1_score")){ 
                           metric.setF1Score(Float.parseFloat(registro[1])); 
                        }
                        if(titulo.equals("accuracy_sd")){ 
                            metric.setAccuracySD(Float.parseFloat(registro[1])); 
                        }
                        if(titulo.equals("f1_score_sd")){ 
                            metric.setF1ScoreSD(Float.parseFloat(registro[1])); 
                        }
                        if(titulo.equals("status")){ 
                            metric.setStatus(registro[1]); 
                        }
                    }    
                    
                }                          
                
            }
          
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }

        return metric;
        
    }

}