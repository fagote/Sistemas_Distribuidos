import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Servidor {

	private static Socket socket;
	private static ServerSocket server;

	private static DataInputStream entrada;
	private static DataOutputStream saida;

	private int porta = 1047;
	
	public final static Path path = Paths			
			.get("src\\fortune-br.txt");
	
	public class FileReader {

		public int countFortunes() throws FileNotFoundException {

			int lineCount = 0;

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();

				}// fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return lineCount;
		}

		public void criaHashMap(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			InputStream is = new BufferedInputStream(new FileInputStream(
					path.toString()));
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					is))) {

				int lineCount = 0;

				String line = "";
				while (!(line == null)) {

					if (line.equals("%"))
						lineCount++;

					line = br.readLine();
					StringBuffer fortune = new StringBuffer();
					while (!(line == null) && !line.equals("%")) {
						fortune.append(line + "\n");
						line = br.readLine();
					}

					hm.put(lineCount, fortune.toString());
				}// fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}
		
		public String read(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			//SEU CODIGO AQUI
			Random random = new Random();
			
			int num_fortune = hm.size()-1;// -1, pois está pegando um indice a mais na função size
			int indice = random.nextInt(num_fortune);
			return hm.get(indice);
		}

		public void write(HashMap<Integer, String> hm, String mensagem)
				throws FileNotFoundException {

			//SEU CODIGO AQUI
			try {

	            FileWriter fileWriter = new FileWriter("src\\fortune-br.txt", true);
	            PrintWriter printWriter = new PrintWriter(fileWriter);
	            
	            printWriter.print("\n%\n"+mensagem);

	            printWriter.close();
	            fileWriter.close();

	        } catch (IOException e) {
	            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
	        }
		}
	}
	
	public String parser(String json) throws FileNotFoundException {
	    FileReader fr = new FileReader();
	    HashMap hm = new HashMap<Integer, String>();
	    fr.criaHashMap(hm);

	    json = json.substring(1, json.length() - 1);
	    String[] campos = json.split(",");
	    String method = "";
	    String args = "";
	    String saida = "";

	    for (String campo : campos) {
	        String[] partes = campo.split(":");
	        String chave = partes[0].trim().replace("\"", "");
	        String valor = partes[1].trim().replace("\"", "");
	        
	        
	        if (chave.equals("method")) {
	            method = valor;
	        } else if (chave.equals("args")) {
	            args = valor;
	        }
	    }


	    // Verificar se method é nulo antes de acessá-lo
	    if(method != null) {
	        //executar o comando
	        if(method.equals("read")) {
	            String fortuna = fr.read(hm);
	            saida = "{\n"
                        + "\"result\": \"" + fortuna+"\"\n}";
	        } else if(method.equals("write")) {
	            if(!args.isEmpty()) {
	            	args = args.replace("[", "").replace("]", "");
	                fr.write(hm, args);
	                saida = "{\n"
	                        + "\"result\": \"" + args + "\""
	                        + "\n}";
	            } else {
	                saida = "{"
	                        + "\"result\": \"false\""
	                        + "}";
	            }
	        }
	    }
	    return saida;
	}

		
	public void iniciar() {
		System.out.println("Servidor iniciado na porta: " + porta);
		
		try {
			// Criar porta de recepcao
			server = new ServerSocket(porta);
			socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes

			// Criar os fluxos de entrada e saida
			entrada = new DataInputStream(socket.getInputStream());
			saida = new DataOutputStream(socket.getOutputStream());

			// Recebimento do valor inteiro
			String in = entrada.readUTF();
			System.out.println(in);
			String saida_parser = parser(in);

			// Processamento do valor
			System.out.println(saida_parser);

			// Envio dos dados (resultado)
			saida.writeUTF(saida_parser);

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		new Servidor().iniciar();

	}

}