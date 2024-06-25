package br.edu.utfpr.projetosistemasdistribuidos;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 3000);
            ICompartilhado compartilhado = (ICompartilhado) registry.lookup("Compartilhado");

            // Cria 20 threads de clientes
            for (int i = 1; i <= 20; i++) {
                String clienteId = "Cliente" + i;
                ClienteRunnable clienteRunnable = new ClienteRunnable(clienteId, compartilhado);
                Thread clienteThread = new Thread(clienteRunnable);
                clienteThread.start();
            }

        } catch (Exception e) {
            System.err.println("Exceção no cliente principal: " + e.toString());
            e.printStackTrace();
        }
    }
}
