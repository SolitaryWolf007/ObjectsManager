package com.ericsonradaelli.objectsmanager.services;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import com.ericsonradaelli.objectsmanager.helpers.Database;
import com.ericsonradaelli.objectsmanager.entities.Manutencao;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 *
 * @author erics
 */
public class ManutencaoService {

    private static final Database db = Database.getInstance("object_manager.db");
    
    // INSERT MANUTENCAO =======================================================
    
    public static int cadastro(int mObjeto, String mDescricao) {
        
        int statusCode = 0;
        
        if(!mDescricao.isBlank()){
            //if( EmprestimoService.checkAvaliable(mObjeto) ){
                try {
                    Statement statement = db.getStatement();

                    statement.execute("INSERT INTO manutencoes(id_objeto,descricao,status,data_entrada) VALUES('"+mObjeto+"','"+mDescricao+"',2,DATETIME('now','localtime'));");
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                    statusCode = 3;
                }
            //}else{
            //    statusCode = 2;
            //}
        }else{
            statusCode = 1;
        }
        
        return statusCode;
    }
    
    // GET MANUTENCOES =========================================================
    
    public static ArrayList<Manutencao> getAll() {
        ArrayList<Manutencao> manutencoes = new ArrayList<>();
            
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM manutencoes;");           
            
            while (results.next()) {
                
                Timestamp de = results.getTimestamp("data_entrada");
                Timestamp ds = results.getTimestamp("data_saida");
                LocalDateTime te = null;
                LocalDateTime ts = null;
                if(de != null){ te = de.toLocalDateTime(); }
                if(ds != null){ ts = ds.toLocalDateTime(); }
                
                Manutencao manut = new Manutencao(
                    results.getInt("id"),
                    results.getInt("id_objeto"),
                    results.getString("descricao"),
                    results.getInt("status"),
                    te,
                    ts
                );    
                
                manutencoes.add(manut);           
            }
            results.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return manutencoes;
    }

    public static Manutencao get(int id) {
        Manutencao manut = null;
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM manutencoes WHERE id = "+id+";");           
            
            if (results.next()) {
                Timestamp de = results.getTimestamp("data_entrada");
                Timestamp ds = results.getTimestamp("data_saida");
                LocalDateTime te = null;
                LocalDateTime ts = null;
                if(de != null){ te = de.toLocalDateTime(); }
                if(ds != null){ ts = ds.toLocalDateTime(); }
                
                manut = new Manutencao(
                    results.getInt("id"),
                    results.getInt("id_objeto"),
                    results.getString("descricao"),
                    results.getInt("status"),
                    te,
                    ts
                );
            }
            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return manut;
    }
    
    public static boolean inService(int mObjeto) {
        boolean service = false;
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM manutencoes WHERE id = "+mObjeto+" AND status > 0;");           

            if ( results.next() ) {
                service = true;            
            }
            
            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return service;
    }
    
    // UPDATE MANUTENCAO =======================================================
    
    public static boolean update(int id, String field, String strValue ) {
        try {
            Statement statement = db.getStatement();

            statement.execute("UPDATE manutencoes SET "+field+" = '"+strValue+"' WHERE id = "+id+";");  

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

            statement.execute("UPDATE manutencoes SET "+field+" = "+intValue+" WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateDate(int id, String field) {
        try {
            Statement statement = db.getStatement();

            statement.execute("UPDATE manutencoes SET "+field+" = DATETIME('now','localtime') WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // DELETE MANUTENCAO =======================================================
    
    public static boolean delete(int id) {
        try {
            Statement statement = db.getStatement();

            statement.execute("DELETE FROM manutencoes WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}