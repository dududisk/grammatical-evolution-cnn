
package dissertacao.classes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String USER = "root"; 
    private static final String PASSWORD = "";
    private static final String URL_DB = "jdbc:mysql://127.0.0.1:3306/mestrado_gmetrics?useTimezone=true&serverTimezone=UTC";
    
    public static Connection retornaConexao(){
        Connection conexao = null;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = DriverManager.getConnection(URL_DB, USER, PASSWORD);            
        }catch(SQLException e){
            System.err.println("Erro de Conexao:" + e.toString());
        } catch (ClassNotFoundException e) {            
            System.err.println("Classe Não Encontrada:" + e.toString());
        }
        
        return conexao;
    }
    

}