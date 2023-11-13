package com.ericsonradaelli.objectsmanager.menus;

import com.ericsonradaelli.objectsmanager.helpers.Entrada;

/**
 *
 * @author erics
 */
public class MenuEmprestimos {
    
    public static void show() {

        boolean exitMenu = false;

        while (!exitMenu){        
            String menuText = 
                "===========================[ Object Manager | Empréstimos ]===========================\n"+
                "1) Incluir Empréstimo\n"+
                "2) Consultar Empréstimo\n"+
                "3) Alterar Empréstimo\n"+
                "4) Excluir Empréstimo\n"+
                "5) Listar Empréstimos...\n"+
                "\n0) Menu Principal";

            int option = Entrada.leiaInt(menuText);

            switch (option) {
                case 0:
                    exitMenu = true;
                    break;
                case 1:
                    //CadastrarPessoa();
                    break;
                case 2:
                    //ConsultarPessoa();
                    break;
                case 3:
                    //AlterarPessoa();
                    break;
                case 4:
                    //ExcluirPessoa();
                    break;
                case 5:
                    //ListarPessoas();
                    break;
            }
            
        }
    }
}
