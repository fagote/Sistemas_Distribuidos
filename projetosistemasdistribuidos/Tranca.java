package br.edu.utfpr.projetosistemasdistribuidos;

public class Tranca {
    public String tipo;
    public String idImovel;

    public Tranca(String tipo, String idImovel) {
        this.tipo = tipo;
        this.idImovel = idImovel;
    }

    @Override
    public String toString() {
        return tipo + " " + idImovel;
    }
}
