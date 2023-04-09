

package dissertacao.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MetricDAO {

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
                
        String sql = "INSERT INTO core_metrics (grammar, dataset, phenotype, accuracy, f1_score, accuracy_sd, f1_score_sd, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try{

            Connection conexao = ConexaoDB.retornaConexao();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, grammar);
            stmt.setString(2, dataset);
            stmt.setString(3, phenotype);
            stmt.setString(4, accuracy);
            stmt.setString(5, f1_score);
            stmt.setString(6, accuracy_sd);
            stmt.setString(7, f1_score_sd);
            stmt.setString(8, status);
            stmt.execute();

            conexao.close();
        
        }catch (Exception e) {
            System.err.println("Erro ao Inserir Phenotype para Treinamento no Banco de Dados: "+e.toString());
        }        
        
    }

    public static Metric getMetric(String grammar, String dataset, String phenotype)
    {       

        Metric metric = new Metric();

        String sql = "SELECT * FROM core_metrics WHERE grammar = ? AND dataset = ? AND phenotype = ?";
          
        try{
            Connection conexao = ConexaoDB.retornaConexao();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, grammar);   
            stmt.setString(2, dataset);   
            stmt.setString(3, phenotype);   
            
            ResultSet registro = stmt.executeQuery();            
            
            while (registro.next()) {
                metric.setGrammar(registro.getString("grammar"));
                metric.setDataset(registro.getString("dataset"));
                metric.setPhenotype(registro.getString("phenotype"));
                metric.setAccuracy(registro.getFloat("accuracy"));
                metric.setF1Score(registro.getFloat("f1_score"));
                metric.setAccuracySD(registro.getFloat("accuracy_sd"));
                metric.setF1ScoreSD(registro.getFloat("f1_score_sd"));
                metric.setStatus(registro.getString("status"));
            } 
            
            conexao.close();
                         
        }catch(Exception e){
            System.err.println("Erro na consulta do Phenotype no Banco de Dados: "+e.toString());
        }

        return metric;            
        
    }

}