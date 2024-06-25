package br.edu.utfpr.projetosistemasdistribuidos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ICompartilhado extends Remote {
    void adquirirTranca(ArrayList<Operacao> operacoes, String clienteId) throws RemoteException;
    void liberarTrancas(ArrayList<Operacao> operacoes, String clienteId) throws RemoteException;
    void agendarImovel(String idImovel, String clienteId) throws RemoteException;
    void comprarImovel(String idImovel, String clienteId) throws RemoteException;
}
