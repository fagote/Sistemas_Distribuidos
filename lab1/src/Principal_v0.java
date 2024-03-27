/**
 * Lab0: Leitura de Base de Dados Não-Distribuida
 * 
 * Autor: Lucio A. Rocha
 * Ultima atualizacao: 20/02/2023
 * 
 * Referencias: 
 * https://docs.oracle.com/javase/tutorial/essential/io
 * 
 */

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;


public class Principal_v0 {

	public final static Path path = Paths			
			.get("src\\fortune-br.txt");
	private int NUM_FORTUNES = 0;

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

				//System.out.println(lineCount);
			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
			return lineCount;
		}

		public void parser(HashMap<Integer, String> hm)
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
						// System.out.print(lineCount + ".");
					}

					hm.put(lineCount, fortune.toString());
					//System.out.println(fortune.toString());

					//System.out.println(lineCount);
				}// fim while

			} catch (IOException e) {
				System.out.println("SHOW: Excecao na leitura do arquivo.");
			}
		}

		public void read(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			//SEU CODIGO AQUI
			Random random = new Random();
			
			int num_fortune = hm.size()-1;// -1, pois está pegando um indice a mais na função size
			System.out.println(num_fortune);
			int indice = random.nextInt(num_fortune);
			System.out.println("Sua fortuna aleatoria tem posição {"+indice+"} no HashMap: \n" +hm.get(indice));
		}

		public void write(HashMap<Integer, String> hm)
				throws FileNotFoundException {

			//SEU CODIGO AQUI
			try {
				Scanner scan = new Scanner(System.in);
	            System.out.println("Escreva a sua fortuna: ");
	            String mensagem = scan.nextLine();
				
	            // Inicializa um FileWriter em modo de adição para adicionar ao final do arquivo
	            FileWriter fileWriter = new FileWriter("src\\fortune-br.txt", true);

	            // Inicializa um PrintWriter para escrever no arquivo
	            PrintWriter printWriter = new PrintWriter(fileWriter);
	            
	            // Adiciona uma mensagem ao final do arquivo
	            printWriter.print("\n%\n"+mensagem);
	            //printWriter.println();

	            // Fecha o PrintWriter e o FileWriter após a escrita
	            printWriter.close();
	            fileWriter.close();

	            System.out.println("Mensagem adicionada ao final do arquivo com sucesso.");
	        } catch (IOException e) {
	            System.out.println("Erro ao escrever no arquivo: " + e.getMessage());
	        }
		}
	}

	public void iniciar() {

		FileReader fr = new FileReader();
		try {
			NUM_FORTUNES = fr.countFortunes();
			HashMap hm = new HashMap<Integer, String>();
			fr.parser(hm);
			fr.read(hm);
			fr.write(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Principal_v0().iniciar();
	}

}