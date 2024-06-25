package br.edu.utfpr.projetosistemasdistribuidos;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Compartilhado extends UnicastRemoteObject implements ICompartilhado {
    private ConcurrentHashMap<String, ArrayList<Tranca>> transacoes;
    private ArrayList<Imovel> imoveisDisponiveis;
    private ConcurrentHashMap<String, Boolean> imoveisEmTransacao;

    public Compartilhado() throws RemoteException {
        super();
        transacoes = new ConcurrentHashMap<>();
        imoveisDisponiveis = new ArrayList<>();
        imoveisEmTransacao = new ConcurrentHashMap<>();
        // Adicione alguns imóveis para exemplo
        adicionarNovoImovel(new Imovel("1", "Apartamento no centro"));
        adicionarNovoImovel(new Imovel("2", "Casa na praia"));
    }

    private void adicionarNovoImovel(Imovel imovel) {
        imoveisDisponiveis.add(imovel);
        imoveisEmTransacao.put(imovel.getId(), false);
    }

    private Imovel obterProximoImovel() {
        if (!imoveisDisponiveis.isEmpty()) {
            return imoveisDisponiveis.remove(0);
        }
        return null;
    }

    @Override
    public synchronized void adquirirTranca(ArrayList<Operacao> operacoes, String clienteId) throws RemoteException {
        boolean conflito;
        ArrayList<Tranca> trancasCliente = new ArrayList<>();
        for (Operacao operacao : operacoes) {
            Tranca tranca = new Tranca(operacao.tipo, operacao.idImovel);
            trancasCliente.add(tranca);
        }
        do {
            conflito = false;
            for (Operacao operacao : operacoes) {
                for (Tranca tranca : transacoes.getOrDefault(operacao.idImovel, new ArrayList<>())) {
                    if (!tranca.tipo.equals(operacao.tipo)) {
                        conflito = true;
                        break;
                    }
                }
                if (conflito) break;
            }
            if (conflito) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.err.println("Thread " + clienteId + " interrompida.");
                    e.printStackTrace();
                }
            }
        } while (conflito);

        transacoes.put(clienteId, trancasCliente);
        for (Operacao operacao : operacoes) {
            System.out.println(clienteId + ": adquiriu tranca para " + operacao.tipo + " no imóvel " + operacao.idImovel);
        }
    }

    @Override
    public synchronized void liberarTrancas(ArrayList<Operacao> operacoes, String clienteId) throws RemoteException {
        ArrayList<Tranca> trancasCliente = transacoes.get(clienteId);
        if (trancasCliente != null) {
            for (Operacao operacao : operacoes) {
                Iterator<Tranca> it = trancasCliente.iterator();
                while (it.hasNext()) {
                    Tranca tranca = it.next();
                    if (tranca.idImovel.equals(operacao.idImovel)) {
                        it.remove();
                        System.out.println(clienteId + ": liberou tranca para " + operacao.tipo + " no imóvel " + operacao.idImovel);
                    }
                }
            }
            transacoes.remove(clienteId);
            notifyAll();
        }
    }

    @Override
    public synchronized void agendarImovel(String idImovel, String clienteId) throws RemoteException {
        if (imoveisEmTransacao.get(idImovel) != null && !imoveisEmTransacao.get(idImovel)) {
            imoveisEmTransacao.put(idImovel, true);
            System.out.println(clienteId + " agendou o imóvel " + idImovel);
        } else {
            System.out.println("Imóvel " + idImovel + " não está disponível para agendamento.");
        }
    }

    @Override
    public synchronized void comprarImovel(String idImovel, String clienteId) throws RemoteException {
        if (imoveisEmTransacao.get(idImovel) != null && imoveisEmTransacao.get(idImovel)) {
            imoveisEmTransacao.put(idImovel, false);
            System.out.println(clienteId + " comprou o imóvel " + idImovel);
            // Remover o imóvel comprado e adicionar um novo imóvel
            imoveisEmTransacao.remove(idImovel);
            Imovel novoImovel = obterProximoImovel();
            if (novoImovel != null) {
                imoveisEmTransacao.put(novoImovel.getId(), false);
                System.out.println("Novo imóvel disponível: " + novoImovel.getId() + " - " + novoImovel.getDescricao());
            } else {
                System.out.println("Nenhum novo imóvel disponível para substituir o imóvel comprado.");
            }
        } else {
            System.out.println("Imóvel " + idImovel + " não está disponível para compra.");
        }
    }
}
