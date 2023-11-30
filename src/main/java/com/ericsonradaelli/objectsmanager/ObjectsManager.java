package com.ericsonradaelli.objectsmanager;

import com.ericsonradaelli.objectsmanager.helpers.Database;
import com.ericsonradaelli.objectsmanager.helpers.Entrada;
import com.ericsonradaelli.objectsmanager.menus.MenuEmprestimos;
import com.ericsonradaelli.objectsmanager.menus.MenuListagem;
import com.ericsonradaelli.objectsmanager.menus.MenuManutencoes;
import com.ericsonradaelli.objectsmanager.menus.MenuObjetos;
import com.ericsonradaelli.objectsmanager.menus.MenuPessoa;
import com.ericsonradaelli.objectsmanager.menus.MenuTipoObjeto;

/**
 *
 * @author ERICSON ARTHUR RADAELLI
 * @entrega 30/11/2023 ( Atividade 3 )
 * @modulo PROJETO INTEGRADOR: PROGRAMAÇÃO DE COMPUTADORES - REF 395849
 * @professor EVANDRO FRANZEN
 * @tutor JEAN CARLO SOUZA KRAHL
 */
public class ObjectsManager {

    public static void main(String[] args) {
        Database db = Database.getInstance("object_manager.db"); // Carrega a a lib do banco de dados
        
        // inicializa as tabelas do banco de dados, em caso de erro, fecha o programa
        if( !db.init() ){ 
            Entrada.leiaBoolean("Houve um problema ao iniciar o Banco de Dados. Verifique o console para encontrar erros.\nO programa será encerrado.","OK","Fechar");
            System.exit(0);
            return;
        }
        
        // menu principal
        boolean exitApp = false;
        while (!exitApp){
            String menuText = 
            "==================================[ Object Manager ]==================================\n"
            +
            "1) Cadastro Pessoas\n"+
            "2) Cadastro Objetos\n"+
            "3) Cadastro Tipo de Objetos\n"+
            "4) Cadastro Empréstimos\n"+
            "5) Cadastro Manutenções\n"+
            "\n0) Fechar Programa";

            int option = Entrada.leiaInt(menuText);
            
            switch (option) {
                case 0:
                    exitApp = true;
                    break;
                case 1:
                    // Ok
                    MenuPessoa.show();
                    break;
                case 2:
                    //Ok
                    MenuObjetos.show();
                    break;
                case 3:
                    // Ok
                    MenuTipoObjeto.show();
                    break;
                case 4:
                    // Ok
                    MenuEmprestimos.show();
                    break;
                case 5:
                    // Ok
                    MenuManutencoes.show();
                    break;
            }
        }
        
        // sistema encerrando
        System.out.println("Stopped Program");
        System.exit(0);
    }
}
