package br.edu.utfpr.projetosistemasdistribuidos;

import java.io.Serializable;

public class Operacao implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String AGENDAMENTO = "agendamento";
    public static final String COMPRA = "compra";

    public String tipo;
    public String idImovel;

    public Operacao(String tipo, String idImovel) {
        this.tipo = tipo;
        this.idImovel = idImovel;
    }

    @Override
    public String toString() {
        return tipo + " " + idImovel;
    }
}
