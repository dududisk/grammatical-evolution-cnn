

package dissertacao.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException; 

public class Output {
      

    public static void prepare(String path, String file_name) throws IOException {

        File arquivo = new File(path+file_name+".csv");
        
        try {
            if (arquivo.exists()) {
                arquivo.delete();
            }
            arquivo.createNewFile();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    public static void read(String path, String file_name) throws IOException {
        File arquivo = new File(path+file_name+".csv");
        
        try {
            if (arquivo.exists()) {
                FileReader fr = new FileReader(arquivo);
                BufferedReader br = new BufferedReader(fr);
                //enquanto houver mais linhas
                while (br.ready()) {
                    //lê a proxima linha
                    String linha = br.readLine();
                    //faz algo com a linha
                    System.out.println(linha);
                }

                br.close();
                fr.close();

            }else{
                System.out.println("Arquivo não encontrado");
            }
                
        } catch (IOException ex) {
                ex.printStackTrace();
        }
            
            
       
	}

	public static void write(String path, String file_name, String text) throws IOException {
        File arquivo = new File(path+file_name+".csv");
        
        try {
            if (!arquivo.exists()) {
                //cria um arquivo (vazio)
                arquivo.createNewFile();
            }
            //caso seja um diretório, é possível listar seus arquivos e diretórios
            File[] arquivos = arquivo.listFiles();
            //escreve no arquivo
            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text.replace( "-" , ""));
            bw.newLine();
            bw.close();
            fw.close();
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       
	}

}