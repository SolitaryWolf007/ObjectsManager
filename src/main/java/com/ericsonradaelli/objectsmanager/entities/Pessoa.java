package com.ericsonradaelli.objectsmanager.entities;

/**
 *
 * @author erics
 */
public class Pessoa {
       
    private int id;
    private String documento;
    private String nome;
    private String telefone;
    
    public Pessoa(int id, String documento, String nome, String telefone) {
        this.id = id;       
        this.documento = documento;
        this.nome = nome;
        this.telefone = telefone;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}
