package com.ericsonradaelli.objectsmanager.services;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.ericsonradaelli.objectsmanager.helpers.Database;
import com.ericsonradaelli.objectsmanager.entities.Emprestimo;
import java.sql.PreparedStatement;
/**
 *
 * @author erics
 */
public class EmprestimoService {

    private static final Database db = Database.getInstance("object_manager.db");
    
    // INSERT EMPRESTIMO =======================================================
    
    public static int cadastro(int objetoId, int pessoaId) {
        
        int statusCode = 0;
        
        try {
            Statement statement = db.getStatement();

            statement.execute("INSERT INTO emprestimos(id_objeto,id_pessoa,ativo,data_retirada) VALUES("+objetoId+","+pessoaId+",1,DATETIME('now','localtime'));");
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            statusCode = 3;
        }
        
        return statusCode;
    }
    
    // GET EMPRESTIMOS =========================================================
    
    public static ArrayList<Emprestimo> getAll() {
        ArrayList<Emprestimo> emprestimos = new ArrayList<>();
            
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM emprestimos;");           
            
            while (results.next()) {
                
                Timestamp dr = results.getTimestamp("data_retirada");
                Timestamp dd = results.getTimestamp("data_devolvido");
                LocalDateTime tr = null;
                LocalDateTime td = null;
                if(dr != null){ tr = dr.toLocalDateTime(); }
                if(dd != null){ td = dd.toLocalDateTime(); }
                
                Emprestimo emp = new Emprestimo(
                    results.getInt("id"),
                    results.getInt("id_objeto"),
                    results.getInt("id_pessoa"),
                    results.getBoolean("ativo"),
                    tr,
                    td
                );    
                
                emprestimos.add(emp);           
            }
            results.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emprestimos;
    }

    public static Emprestimo get(int id) {
        Emprestimo emp = null;
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM emprestimos WHERE id = "+id+";");           
            
            if (results.next()) {
                Timestamp dr = results.getTimestamp("data_retirada");
                Timestamp dd = results.getTimestamp("data_devolvido");
                LocalDateTime tr = null;
                LocalDateTime td = null;
                if(dr != null){ tr = dr.toLocalDateTime(); }
                if(dd != null){ td = dd.toLocalDateTime(); }
                
                emp = new Emprestimo(
                    results.getInt("id"),
                    results.getInt("id_objeto"),
                    results.getInt("id_pessoa"),
                    results.getBoolean("ativo"),
                    tr,
                    td
                ); 
            }
            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return emp;
    }
    
    public static ArrayList<Emprestimo> getByDate(String strData) {      
        ArrayList<Emprestimo> emprestimos = new ArrayList<>();
        try {
            
            PreparedStatement statement = db.prepareStatement("SELECT * FROM emprestimos WHERE STRFTIME('%d/%m/%Y',data_retirada) = ? OR STRFTIME('%d/%m/%Y',data_devolvido) = ?;"); 
            statement.setString(1, strData);
            statement.setString(2, strData);   
            ResultSet results = statement.executeQuery();       
            
            while (results.next()) {
                Timestamp dr = results.getTimestamp("data_retirada");
                Timestamp dd = results.getTimestamp("data_devolvido");
                LocalDateTime tr = null;
                LocalDateTime td = null;
                if(dr != null){ tr = dr.toLocalDateTime(); }
                if(dd != null){ td = dd.toLocalDateTime(); }

                Emprestimo emp = new Emprestimo(
                    results.getInt("id"),
                    results.getInt("id_objeto"),
                    results.getInt("id_pessoa"),
                    results.getBoolean("ativo"),
                    tr,
                    td
                );    
                emprestimos.add(emp);           
            }

            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprestimos;
    }
    
    public static boolean isAvaliable(int mObjeto) {
        boolean avaliable = true;
        try {
            Statement statement = db.getStatement();
            
            ResultSet results = statement.executeQuery("SELECT * FROM emprestimos WHERE id_objeto = "+mObjeto+" AND ativo > 0;");           

            if ( results.next() ) {
                avaliable = false;            
            }
            
            results.close();
            statement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return avaliable;
    }
    
    // UPDATE EMPRESTIMO =======================================================
    
    public static boolean update(int id, String field, String strValue ) {
        try {
            Statement statement = db.getStatement();

            statement.execute("UPDATE emprestimos SET "+field+" = '"+strValue+"' WHERE id = "+id+";");  

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

            statement.execute("UPDATE emprestimos SET "+field+" = "+intValue+" WHERE id = "+id+";");  

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

            statement.execute("UPDATE emprestimos SET "+field+" = DATETIME('now','localtime') WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // DELETE EMPRESTIMO =======================================================
    
    public static boolean delete(int id) {
        try {
            Statement statement = db.getStatement();

            statement.execute("DELETE FROM emprestimos WHERE id = "+id+";");  

            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}