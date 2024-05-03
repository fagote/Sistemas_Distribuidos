/**
 * Laboratorio 3  
 * Autor: Ian e Leonardo Fagote
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorImpl implements IMensagem{
	
	private Principal p;
    
    public ServidorImpl() {
    	this.p = new Principal();
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
    
    public String parserJSON(String json) throws Exception {
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
	            String fortuna = p.read();
	            saida = "{\n"
                        + "\"result\": \"" + fortuna+"\"\n}";
	        } else if(method.equals("write")) {
	            if(!args.isEmpty()) {
	            	args = args.replace("[", "").replace("]", "");
	                p.write(args);
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
	
    public void iniciar(){

    try {
            Registry servidorRegistro = LocateRegistry.createRegistry(1099);            
            IMensagem skeleton  = (IMensagem) UnicastRemoteObject.exportObject(this, 0); //0: sistema operacional indica a porta (porta anonima)
            servidorRegistro.rebind("servidorFortunes", skeleton);
            System.out.print("Servidor RMI: Aguardando conexoes...");
                        
        } catch(Exception e) {
            e.printStackTrace();
        }        

    }
    
    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl();
        servidor.iniciar();
    }    
}
