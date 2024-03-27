
/**
 * Laboratorio 2 de Sistemas Distribuidos
 * 
 * Autor: Ian Ferranti e Leonardo Fagote
 * Ultima atualizacao: 25/03/2024
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Cliente {
    
    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    
    private int porta=1047;
    
    public String read() {

    	String json = "{\n\"method\": \"read\",\n\"args\": [\"\"]\n}";
    	return json;
	}
    
    public String write() {
    	
    	Scanner scan = new Scanner(System.in);
        System.out.println("Escreva a sua fortuna: ");
        String mensagem = scan.nextLine();

    	String json = "{\n\"method\": \"write\",\n\"args\": [\""+mensagem+"\"]\n}";
    	return json;
	}
    
    
    public void iniciar(){
    	System.out.println("Cliente iniciado na porta: "+porta);
    	
    	try {
            
            socket = new Socket("127.0.0.1", porta);
            
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
            
            //Recebe do usuario algum valor
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Digite \"write\" para escrever uma fortuna");
            System.out.println("Digite \"read\" para ler uma fortuna aleatoria");
            String escolha = br.readLine();
            
            String enviar = "";
            if(escolha.equals("write")) {
            	enviar = write();
            }
            
            if(escolha.equals("read")) {
            	enviar = read();
            }
            
            //O valor eh enviado ao servidor
            System.out.println(enviar);
            saida.writeUTF(enviar);
            
            //Recebe-se o resultado do servidor
            String resultado = entrada.readUTF();
            
            //Mostra o resultado na tela
            System.out.println(resultado);
            
            socket.close();
            
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Cliente().iniciar();
    }
    
}
