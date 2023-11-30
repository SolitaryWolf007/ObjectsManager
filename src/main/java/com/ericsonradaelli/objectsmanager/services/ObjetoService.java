package com.ericsonradaelli.objectsmanager.services;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.ericsonradaelli.objectsmanager.helpers.Database;
import com.ericsonradaelli.objectsmanager.entities.Objeto;
import java.util.Collections;
import java.util.Comparator;


/**
 *
 * @author erics
 */
public class ObjetoService {

    private static final Database db = Database.getInstance("object_manager.db");
    
    // INSERT OBJETO ===========================================================
    
    public static int cadastro(String oNome, String oDescricao, int oTipo) {
        int statusCode = 0;
        
        if(!oNome.isBlank() && !oDescricao.isBlank()){
            try {
                Statement statement = db.getStatement();

                statement.execute("INSERT INTO objetos(nome,descricao,tipo_id,ativo) VALUES('"+oNome+"','"+oDescricao+"',"+oTipo+",1);");
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
    
    // GET OBJETOS =============================================================
    
    public static ArrayList<Objeto> getAll() {
        ArrayList<Objeto> objetos = new ArrayList<>();
            
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM objetos;");           
            
            while (results.next()) {
                
                Objeto objeto = new Objeto(
                    results.getInt("id"),
                    results.getString("nome"),
                    results.getString("descricao"),
                    results.getInt("tipo_id"),
                    results.getBoolean("ativo")
                );     
                
                objetos.add(objeto);           
            }
            results.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return objetos;
    }

    public static Objeto get(int id) {
        Objeto objeto = null;
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM objetos WHERE id = "+id+";");           
            
            if (results.next()) {
                objeto = new Objeto(
                    results.getInt("id"),
                    results.getString("nome"),
                    results.getString("descricao"),
                    results.getInt("tipo_id"),
                    results.getBoolean("ativo")
                );            
            }
            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return objeto;
    }
    
    public static ArrayList<Objeto> getObjectsWithFilter(int typeId){
        ArrayList<Objeto> filter = new ArrayList<>();           
        ArrayList<Objeto> objetos = ObjetoService.getAll();  
        Collections.sort(objetos, Comparator.comparing(Objeto::getNome));
        for (Objeto objeto : objetos) {
            if(typeId > 0){
                if(objeto.getTipo() == typeId){
                    filter.add(objeto);
                }
            }else{
                filter.add(objeto);
            }
        }
        return filter;
    }
    
    // UPDATE OBJETO ===========================================================
    
    public static boolean update(int id, String field, String strValue ) {
        try {
            Statement statement = db.getStatement();

            statement.execute("UPDATE objetos SET "+field+" = '"+strValue+"' WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean update(int id, String field, int intValue ) {
        try {
            Statement statement = db.getStatement();

            statement.execute("UPDATE objetos SET "+field+" = "+intValue+" WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // DELETE OBJETO ===========================================================
    
    public static boolean delete(int id) {
        try {
            Statement statement = db.getStatement();

            statement.execute("DELETE FROM objetos WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}