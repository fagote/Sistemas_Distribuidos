/**
 * Laboratorio 3  
 * Autor: Ian e Leonardo Fagote
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class ClienteRMI {
    
    public static void main(String[] args) {
                
        try {
                        
            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);            
            IMensagem stub =
                    (IMensagem) registro.lookup("servidorFortunes");  
                        
            String opcao="";
            Scanner leitura = new Scanner(System.in);
            do {
            	System.out.println("1) Read");
            	System.out.println("2) Write");
            	System.out.println("x) Exit");
            	System.out.print(">> ");
            	opcao = leitura.next();
            	switch(opcao){
            	case "1": {
            		Mensagem mensagem = new Mensagem("", opcao);
            		Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'read'
            		System.out.println(resposta.getMensagem());
            		break;
            	}
            	case "2": {
            		
            		leitura.nextLine();
            		//Monta a mensagem                		
            		System.out.print("Add fortune: ");
            		String fortune = leitura.nextLine();
            		
            		Mensagem mensagem = new Mensagem(fortune, opcao);
            		Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'write'
            		System.out.println(resposta.getMensagem());
            		break;
            	}
            	}
            } while(!opcao.equals("x"));
                        
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
