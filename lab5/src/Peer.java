/**
 * Lab05: Sistema P2P
 * 
 * Autor: Ian Ferranti e leonardo Fagote
 * Ultima atualizacao: 09/06/2024
 * 
 * Referencias: 
 * https://docs.oracle.com/javase/tutorial/essential/io
 * http://fortunes.cat-v.org/
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Peer implements IMensagem{
    
	ArrayList<PeerLista> alocados;
	
    public Peer() {
          alocados = new ArrayList<>();
    }
    
    //Cliente: invoca o metodo remoto 'enviar'
    //Servidor: invoca o metodo local 'enviar'
    @Override
    public Mensagem enviar(Mensagem mensagem) throws RemoteException {
        Mensagem resposta;
        try {
        	System.out.println("Mensagem recebida: " + mensagem.getMensagem());
			resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
		} catch (Exception e) {
			e.printStackTrace();
			resposta = new Mensagem("{\n" + "\"result\": false\n" + "}");
		}
        return resposta;
    }    
    
    public String parserJSON(String json) {
		String result = "false";

		String fortune = "-1";		
		
		String[] v = json.split(":");
		System.out.println(">>>" + v[1]);
		String[] v1 = v[1].split("\"");
		System.out.println(">>>" + v1[1]);
		if (v1[1].equals("write")) {
			String [] p = json.split("\\["); 
			 System.out.println(p[1]); 
			 String [] p1 = p[1].split("]"); 
			 System.out.println(p1[0]); 
			 String [] p2 = p1[0].split("\""); 
			 System.out.println(p2[1]); 
			 fortune = p2[1];
			 
			// Write in file
			Principal pv2 = new Principal();
			pv2.write(fortune);
		} else if (v1[1].equals("read")) {
			// Read file
			Principal pv2 = new Principal();
			fortune = pv2.read();
		}

		result = "{\n" + "\"result\": \"" + fortune + "\"" + "}";
		System.out.println(result);

		return result;
	}
    
    public void iniciar() {
        Scanner leitura = null;
        try {
            // Cria uma lista de peers a partir do enum PeerLista
            List<PeerLista> listaPeers = new ArrayList<>();
            for (PeerLista peer : PeerLista.values()) {
                listaPeers.add(peer);
            }

            // Cria ou obtém o registro RMI na porta 1099
            Registry servidorRegistro = LocateRegistry.createRegistry(1099);
            servidorRegistro = LocateRegistry.getRegistry();

            // Exibe os peers disponíveis para o usuário escolher
            System.out.println("Escolha um Peer para iniciar o servidor:");
            for (int i = 0; i < listaPeers.size(); i++) {
                System.out.println((i + 1) + ") " + listaPeers.get(i).getNome());
            }

            // Lê a escolha do usuário com validação de entrada
            leitura = new Scanner(System.in);
            int escolha = 0;
            while (escolha < 1 || escolha > listaPeers.size()) {
                System.out.print("Digite o número do Peer desejado (1-" + listaPeers.size() + "): ");
                if (leitura.hasNextInt()) {
                    escolha = leitura.nextInt();
                } else {
                    leitura.next(); // Descarta a entrada inválida
                    System.out.println("Entrada inválida. Por favor, digite um número entre 1 e " + listaPeers.size() + ".");
                }
            }

            // Obtém o peer escolhido e registra o objeto RMI
            PeerLista peerEscolhido = listaPeers.get(escolha - 1);
            IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0);
            servidorRegistro.rebind(peerEscolhido.getNome(), skeleton);
            System.out.println(peerEscolhido.getNome() + " Servidor RMI: Aguardando conexões...");

            // Inicia o cliente RMI
            new ClienteRMI().iniciarCliente();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (leitura != null) {
                leitura.close();
            }
        }
    }
    
    public static void main(String[] args) {
        Peer servidor = new Peer();
        servidor.iniciar();
    }    
}
