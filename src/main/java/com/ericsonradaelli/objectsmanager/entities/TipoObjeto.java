package com.ericsonradaelli.objectsmanager.entities;

/**
 *
 * @author erics
 */
public class TipoObjeto {
       
    private int id;
    private String descricao;

    public TipoObjeto(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String nome) {
        this.descricao = nome;
    }
}
