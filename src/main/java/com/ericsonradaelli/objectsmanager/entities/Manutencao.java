package com.ericsonradaelli.objectsmanager.entities;

import java.time.LocalDateTime;

/**
 *
 * @author erics
 */
public class Manutencao {
    
    private int id;
    private int id_objeto;
    private String descricao;
    private int status;
    private LocalDateTime data_entrada;
    private LocalDateTime data_saida;

    public Manutencao(int id, int id_objeto, String descricao, int status, LocalDateTime data_entrada, LocalDateTime data_saida) {
        this.id = id;
        this.id_objeto = id_objeto;
        this.descricao = descricao;
        this.status = status;
        this.data_entrada = data_entrada;
        this.data_saida = data_saida;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status;
    }
    
    public String statusToString() {
        String state = "Desconhecido";
        switch(status){
            case 0:
                state = "Concluído";
                break;
            case 1:
                state = "Em serviço";
                break;
            case 2:
                state = "Recebido";
                break;        
        }
        return state;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getDataEntrada() {
        return data_entrada;
    }

    public void setDataEntrada(LocalDateTime data_entrada) {
        this.data_entrada = data_entrada;
    }

    public LocalDateTime getDataSaida() {
        return data_saida;
    }

    public void setDataSaida(LocalDateTime data_saida) {
        this.data_saida = data_saida;
    }
   
}
