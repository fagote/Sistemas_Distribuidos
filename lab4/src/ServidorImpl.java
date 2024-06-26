/**
  * Laboratorio 4  
  * Autor: Ian e Leonardo Fagote
  */
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ServidorImpl implements IMensagem{
    
	private Principal p;
	ArrayList<Peer> alocados;
	
    public ServidorImpl() {
          alocados = new ArrayList<>();
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
    
    public String parserJSON(String json) {
		
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
	            String fortuna = this.p.read();
	            saida = "{\n"
                        + "\"result\": \"" + fortuna+"\"\n}";
	        } else if(method.equals("write")) {
	            if(!args.isEmpty()) {
	            	args = args.replace("[", "").replace("]", "");
	                this.p.write(args);
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
    		//TODO: Adquire aleatoriamente um 'nome' do arquivo Peer.java
    		List<Peer> listaPeers = new ArrayList<Peer>(EnumSet.allOf(Peer.class));
    		
    		Registry servidorRegistro;
    		try {
    			servidorRegistro = LocateRegistry.createRegistry(1099);
    		} catch (java.rmi.server.ExportException e){ //Registro jah iniciado 
    			System.out.print("Registro jah iniciado. Usar o ativo.\n");
    		}
    		servidorRegistro = LocateRegistry.getRegistry(); //Registro eh unico para todos os peers
    		String [] listaAlocados = servidorRegistro.list();
    		for(int i=0; i<listaAlocados.length;i++)
    			System.out.println(listaAlocados[i]+" ativo.");
    		
    		SecureRandom sr = new SecureRandom();
    		Peer peer = listaPeers.get(sr.nextInt(listaPeers.size()));
    		
    		int tentativas=0;
    		boolean repetido = true;
    		boolean cheio = false;
    		while(repetido && !cheio){
    			repetido=false;    			
    			peer = listaPeers.get(sr.nextInt(listaPeers.size()));
    			for(int i=0; i<listaAlocados.length && !repetido; i++){
    				
    				if(listaAlocados[i].equals(peer.getNome())){
    					System.out.println(peer.getNome() + " ativo. Tentando proximo...");
    					repetido=true;
    					tentativas=i+1;
    				}    			  
    				
    			}
    			//System.out.println(tentativas+" "+listaAlocados.length);
    			    			
    			//Verifica se o registro estah cheio (todos alocados)
    			if(listaAlocados.length>0 && //Para o caso inicial em que nao ha servidor alocado,
    					                     //caso contrario, o teste abaixo sempre serah true
    				tentativas==listaPeers.size()){ 
    				cheio=true;
    			}
    		}
    		
    		if(cheio){
    			System.out.println("Sistema cheio. Tente mais tarde.");
    			System.exit(1);
    		}
    		
            IMensagem skeleton  = (IMensagem) UnicastRemoteObject.exportObject(this, 0); //0: sistema operacional indica a porta (porta anonima)
            servidorRegistro.rebind(peer.getNome(), skeleton);
            System.out.print(peer.getNome() +" Servidor RMI: Aguardando conexoes...");
                        
        } catch(Exception e) {
            e.printStackTrace();
        }        

    }
    
    public static void main(String[] args) {
        ServidorImpl servidor = new ServidorImpl();
        servidor.iniciar();
    }    
}
