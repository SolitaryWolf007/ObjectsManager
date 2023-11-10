package com.ericsonradaelli.objectsmanager.services;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.ericsonradaelli.objectsmanager.helpers.Database;
import com.ericsonradaelli.objectsmanager.entities.Pessoa;

/**
 *
 * @author erics
 */
public class PessoaService {
    private static final Database db = Database.getInstance("object_manager.db");
        
    // INSERT PESSOA ===========================================================    
    public static int cadastro(String documento, String nome, String telefone) {
        int statusCode = 0;
        
        if(!documento.isBlank() && !nome.isBlank() && !telefone.isBlank()){
            try {
                Statement statement = db.getStatement();

                ResultSet results = statement.executeQuery("SELECT * FROM pessoas WHERE documento = '"+documento+"';");     
                if (!results.next()){
                    results.close();

                    statement.execute("INSERT INTO pessoas(nome,documento,telefone) VALUES('"+nome+"','"+documento+"','"+telefone+"');");  
                }else{
                    statusCode = 2;
                }

                statement.close();

            } catch (SQLException e) {
                statusCode = 3;
            }
        }else{
            statusCode = 1;
        }
        
        return statusCode;
    }
    
    // GET PESSOAS =============================================================
    
    public static ArrayList<Pessoa> getAll() {
        ArrayList<Pessoa> pessoas = new ArrayList<>();
            
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM pessoas;");           
            
            while (results.next()) {
                Pessoa usuario = new Pessoa(results.getInt("id"),results.getString("documento"),results.getString("nome"),results.getString("telefone"));     
                pessoas.add(usuario);           
            }
            results.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pessoas;
    }
    
    private static Pessoa internalGetPessoa(String sqlStr){   
        Pessoa usuario = null;
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery(sqlStr);           
            
            if (results.next()) {
                usuario = new Pessoa(results.getInt("id"),results.getString("documento"),results.getString("nome"),results.getString("telefone"));            
            }
            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return usuario;
    }
    
    public static Pessoa get(int id) {
        return internalGetPessoa("SELECT * FROM pessoas WHERE id = "+id+";"); 
    }
    
    public static Pessoa get(String documento) {
        return internalGetPessoa("SELECT * FROM pessoas WHERE documento = '"+documento+"';");
    }
    
    // UPDATE PESSOA ===========================================================
    
    public static boolean update(int id, String field, String value ) {
        try {
            Statement statement = db.getStatement();

            statement.execute("UPDATE pessoas SET "+field+" = '"+value+"' WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // DELETE PESSOA ===========================================================
    
    public static boolean delete(int id) {
        try {
            Statement statement = db.getStatement();

            statement.execute("DELETE FROM pessoas WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}