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
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IMensagem extends Remote {
    
    public Mensagem enviar(Mensagem mensagem) throws RemoteException;
    
}
