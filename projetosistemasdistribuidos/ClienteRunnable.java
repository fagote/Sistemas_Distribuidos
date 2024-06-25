package br.edu.utfpr.projetosistemasdistribuidos;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class ClienteRunnable implements Runnable {
    private String clienteId;
    private ICompartilhado compartilhado;
    private ArrayList<Operacao> operacoes;

    public ClienteRunnable(String clienteId, ICompartilhado compartilhado) {
        this.clienteId = clienteId;
        this.compartilhado = compartilhado;
        this.operacoes = new ArrayList<>();
        operacoes.add(new Operacao(Operacao.AGENDAMENTO, "1"));
        operacoes.add(new Operacao(Operacao.COMPRA, "1"));
    }

    @Override
    public void run() {
        try {
            // Adquirir trancas para as operações
            compartilhado.adquirirTranca(operacoes, clienteId);

            // Simula uma operação no imóvel (agendamento)
            System.out.println(clienteId + " realizando agendamento...");
            compartilhado.agendarImovel("1", clienteId);

            // Simula uma operação no imóvel (compra)
            System.out.println(clienteId + " realizando compra...");
            compartilhado.comprarImovel("1", clienteId);

            // Liberar trancas após as operações
            compartilhado.liberarTrancas(operacoes, clienteId);

        } catch (Exception e) {
            System.err.println("Exceção no " + clienteId + ": " + e.toString());
            e.printStackTrace();
        }
    }
}
