package br.edu.utfpr.projetosistemasdistribuidos;

import java.io.Serializable;

public class Imovel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String descricao;
    private boolean emTransacao;

    public Imovel(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.emTransacao = false;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isEmTransacao() {
        return emTransacao;
    }

    public void setEmTransacao(boolean emTransacao) {
        this.emTransacao = emTransacao;
    }

    @Override
    public String toString() {
        return "Imovel{" + "id='" + id + '\'' + ", descricao='" + descricao + '\'' + ", emTransacao=" + emTransacao + '}';
    }
}
