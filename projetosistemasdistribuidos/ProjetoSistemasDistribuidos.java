package br.edu.utfpr.projetosistemasdistribuidos;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ProjetoSistemasDistribuidos {
    public static void main(String[] args) {
        try {
            Compartilhado server = new Compartilhado();
            Registry registry = LocateRegistry.createRegistry(3000);
            registry.rebind("Compartilhado", server);
            System.out.println("Servidor RMI pronto.");
        } catch (Exception e) {
            System.err.println("Exceção no servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}
