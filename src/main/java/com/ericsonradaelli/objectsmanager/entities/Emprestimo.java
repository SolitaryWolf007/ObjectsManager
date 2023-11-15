package com.ericsonradaelli.objectsmanager.entities;

import java.time.LocalDateTime;

/**
 *
 * @author erics
 */
public class Emprestimo {
    private int id;
    private int id_objeto;
    private int id_pessoa;
    private boolean ativo;
    private LocalDateTime data_retirada;
    private LocalDateTime data_devolvido;

    public Emprestimo(int id, int id_objeto, int id_pessoa, boolean ativo, LocalDateTime data_retirada, LocalDateTime data_devolvido) {
        this.id = id;
        this.id_objeto = id_objeto;
        this.id_pessoa = id_pessoa;
        this.ativo = ativo;
        this.data_retirada = data_retirada;
        this.data_devolvido = data_devolvido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObjeto() {
        return id_objeto;
    }

    public void setObjeto(int id_objeto) {
        this.id_objeto = id_objeto;
    }

    public int getPessoa() {
        return id_pessoa;
    }

    public void setPessoa(int id_pessoa) {
        this.id_pessoa = id_pessoa;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataRetirada() {
        return data_retirada;
    }

    public void setDataRetirada(LocalDateTime data_retirada) {
        this.data_retirada = data_retirada;
    }

    public LocalDateTime getDataDevolvido() {
        return data_devolvido;
    }

    public void setDataDevolvido(LocalDateTime data_devolvido) {
        this.data_devolvido = data_devolvido;
    }
}
