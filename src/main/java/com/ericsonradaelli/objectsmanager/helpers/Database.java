package com.ericsonradaelli.objectsmanager.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Database instance;
    private Connection connection;
    
    private Database(String databaseName) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Database getInstance(String databaseName) {
        if (instance == null) {
            instance = new Database(databaseName);
        }
        return instance;
    }

    public Statement getStatement() {
        try {
            Statement statement = connection.createStatement();
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public PreparedStatement prepareStatement(String sql) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean init() {   
        try {
            Statement statement = connection.createStatement();

            statement.execute("PRAGMA foreign_keys = ON;"); // HABILITAR CHAVES ESTRANGEIRAS NO SQLITE
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS pessoas (
                    id INTEGER,
                    nome VARCHAR,
                    documento VARCHAR,
                    telefone VARCHAR,                            
                    PRIMARY KEY (id),
                    UNIQUE (documento)
                );
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS tipos_objeto (
                    id INTEGER,
                    descricao VARCHAR,
                    PRIMARY KEY (id)
                );                                  
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS objetos (
                    id INTEGER,
                    nome VARCHAR,
                    descricao VARCHAR,
                    tipo_id INTEGER,
                    ativo INTEGER,
                    PRIMARY KEY (id),
                    CONSTRAINT fk_objetos_tipos_objeto FOREIGN KEY (tipo_id) REFERENCES tipos_objeto(id)
                );                                  
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS emprestimos (
                    id INTEGER,
                    id_objeto INTEGER,
                    id_pessoa INTEGER,
                    ativo INTEGER,
                    data_retirada DATETIME,
                    data_devolvido DATETIME,
                    PRIMARY KEY (id),
                    CONSTRAINT fk_emprestimos_objetos FOREIGN KEY (id_objeto) REFERENCES objetos(id) ON UPDATE CASCADE ON DELETE CASCADE,
                    CONSTRAINT fk_emprestimos_pessoas FOREIGN KEY (id_pessoa) REFERENCES pessoas(id) ON UPDATE CASCADE ON DELETE CASCADE
                );                                  
            """);
            
            statement.execute("""
                CREATE TABLE IF NOT EXISTS manutencoes (
                    id INTEGER,
                    id_objeto INTEGER,
                    descricao VARCHAR,
                    status INTEGER,
                    data_entrada DATETIME,
                    data_saida DATETIME,
                    PRIMARY KEY (id),
                    CONSTRAINT fk_manutencoes_objetos FOREIGN KEY (id_objeto) REFERENCES objetos(id) ON UPDATE CASCADE ON DELETE CASCADE
                );                                  
            """);
                        
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}