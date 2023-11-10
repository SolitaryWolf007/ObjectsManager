package com.ericsonradaelli.objectsmanager.services;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.ericsonradaelli.objectsmanager.helpers.Database;
import com.ericsonradaelli.objectsmanager.entities.TipoObjeto;

/**
 *
 * @author erics
 */
public class TipoObjetoService {
    private static final Database db = Database.getInstance("object_manager.db");
        
    // INSERT TIPO =============================================================    
    public static int cadastro(String descricao) {
        int statusCode = 0;
        
        if(!descricao.isBlank()){
            try {
                Statement statement = db.getStatement();

                statement.execute("INSERT INTO tipos_objeto(descricao) VALUES('"+descricao+"');");
                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
                statusCode = 2;
            }
        }else{
            statusCode = 1;
        }
        
        return statusCode;
    }
    
    // GET TIPOS ===============================================================
    
    public static ArrayList<TipoObjeto> getAll() {
        ArrayList<TipoObjeto> tiposObj = new ArrayList<>();
            
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM tipos_objeto;");           
            
            while (results.next()) {
                TipoObjeto tipo = new TipoObjeto(results.getInt("id"),results.getString("descricao"));     
                tiposObj.add(tipo);           
            }
            results.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tiposObj;
    }
    

    public static TipoObjeto get(int id) {
        TipoObjeto tipo = null;
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM tipos_objeto WHERE id = "+id+";");           
            
            if (results.next()) {
                tipo = new TipoObjeto(results.getInt("id"),results.getString("descricao"));            
            }
            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tipo;
    }
    
    
    // UPDATE TIPO =============================================================
    
    public static boolean update(int id, String field, String value ) {
        try {
            Statement statement = db.getStatement();

            statement.execute("UPDATE tipos_objeto SET "+field+" = '"+value+"' WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // DELETE TIPO =============================================================
    
    public static boolean delete(int id) {
        try {
            Statement statement = db.getStatement();

            statement.execute("DELETE FROM tipos_objeto WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}