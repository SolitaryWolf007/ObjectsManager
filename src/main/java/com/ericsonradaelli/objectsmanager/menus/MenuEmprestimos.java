package com.ericsonradaelli.objectsmanager.menus;

import com.ericsonradaelli.objectsmanager.entities.Emprestimo;
import com.ericsonradaelli.objectsmanager.entities.Objeto;
import com.ericsonradaelli.objectsmanager.entities.Pessoa;
import com.ericsonradaelli.objectsmanager.helpers.Entrada;
import com.ericsonradaelli.objectsmanager.services.EmprestimoService;
import com.ericsonradaelli.objectsmanager.services.ManutencaoService;
import com.ericsonradaelli.objectsmanager.services.ObjetoService;
import com.ericsonradaelli.objectsmanager.services.PessoaService;
import java.util.ArrayList;

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
                "5) Listar Empréstimos\n"+
                "\n0) Menu Principal";

            int option = Entrada.leiaInt(menuText);

            switch (option) {
                case 0:
                    exitMenu = true;
                    break;
                case 1:
                    CadastrarEmprestimo();
                    break;
                case 2:
                    ConsultarEmprestimo(true);
                    break;
                case 3:
                    AlterarEmprestimo();
                    break;
                case 4:
                    ExcluirEmprestimo();
                    break;
                case 5:
                    ListarEmprestimos();
                    break;
            }
            
        }
    }
    
    private static void CadastrarEmprestimo() {
        
        //=[ PESSOAS ]========================================
        String listaPessoas = "";
        ArrayList<Pessoa> pessoas = PessoaService.getAll();
        for (Pessoa listPessoa : pessoas) {
            int id = listPessoa.getId();
            listaPessoas += ( "#"+id+" | "+listPessoa.getNome()+"\r\n" );
        }
        int ePessoa = Entrada.leiaInt("===========================[ Object Manager | Empréstimos ]===========================\nCadastro (1/3)\n> Indique o ID da Pessoa\n\n0) Cancelar\n===[ PESSOAS ]===\n"+listaPessoas);
        Pessoa pessoa = PessoaService.get(ePessoa);
        while (pessoa == null){
            if(ePessoa == 0) return;
            ePessoa = Entrada.leiaInt("===========================[ Object Manager | Empréstimos ]===========================\nCadastro (1/3)\n> Pessoa informada é inválida!!\n> Indique o ID da Pessoa\n\n0) Cancelar\n===[ PESSOAS ]===\n"+listaPessoas);
            pessoa = PessoaService.get(ePessoa);
        }
        //=[ OBJETOS ]========================================
        String listaObjetos = "";
        ArrayList<Objeto> objetos = ObjetoService.getAll();
        for (Objeto listObjeto : objetos) {
            int id = listObjeto.getId();
            if ( listObjeto.isAtivo() && !ManutencaoService.inService(id) && EmprestimoService.isAvaliable(id) ){
                listaObjetos += ( "#"+id+" | "+listObjeto.getNome()+" | "+listObjeto.getDescricao()+"\r\n" );
            }
        }
        int eObjeto = Entrada.leiaInt("===========================[ Object Manager | Empréstimos ]===========================\nCadastro (2/3)\n> Indique o ID do Objeto\n\n0) Cancelar\n===[ OBJETOS DISPONÍVEIS ]===\n"+listaObjetos);
        Objeto objeto = ObjetoService.get(eObjeto);
        while (objeto == null){
            if(eObjeto == 0) return;
            eObjeto = Entrada.leiaInt("===========================[ Object Manager | Empréstimos ]===========================\nCadastro (2/3)\n> Objeto informado é inválido!!\n> Indique o ID do Objeto\n\n0) Cancelar\n===[ OBJETOS DISPONÍVEIS ]===\n"+listaObjetos);
            objeto = ObjetoService.get(eObjeto);
        }
            
        //=[ CONFIRMAR ]========================================
        
        String confirmText = "===========================[ Object Manager | Empréstimos ]===========================\n"+
        "Cadastro (3/3)"+
        "\n> Confirme os Dados:"+
        "\n- Pessoa: #"+ePessoa+" ("+pessoa.getNome()+")"+
        "\n- Objeto: #"+eObjeto+" ("+objeto.getNome()+")";

        boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
        if(confirm){

            if ( objeto.isAtivo() && !ManutencaoService.inService(eObjeto) && EmprestimoService.isAvaliable(eObjeto) ){

                int statusCode = EmprestimoService.cadastro(eObjeto, ePessoa);
                String message = "Erro não especificado.";

                switch(statusCode){
                    case 0:
                        message = "O Empréstimo foi cadastrado com sucesso!";
                        break;
                    case 1:
                        message = "Não são permitidos campos vazios!";
                        break;
                    default:
                        message = "Erro no banco de dados!";
                        break;
                }

                Entrada.leiaBoolean(message,"OK","Fechar");
                
            }else{
                Entrada.leiaBoolean("O Objeto indicado não se encontra disponível.","OK","Fechar");
            }
        }          
    }
    
    private static Emprestimo ConsultarEmprestimo(boolean showInfo) {
        Emprestimo targetEmp;
        
        int tId = Entrada.leiaInt("===========================[ Object Manager | Empréstimos ]===========================\n[ Buscar ]\n> Indique o ID do Empréstimo\n");
        targetEmp = EmprestimoService.get(tId);
        
        if(targetEmp != null){
            if(showInfo){
                
                String nomePessoa = "("+String.valueOf(targetEmp.getPessoa())+")";
                Pessoa checkPessoa = PessoaService.get(targetEmp.getPessoa());
                if(checkPessoa != null){ nomePessoa = nomePessoa+" "+checkPessoa.getNome(); }

                String nomeObj = "("+String.valueOf(targetEmp.getObjeto())+")";
                Objeto checkObjeto = ObjetoService.get(targetEmp.getObjeto());
                if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }

                String empEstado = "Devolvido";
                if( targetEmp.isAtivo() ){ empEstado = "Retirado"; }

                String infoText = "===========================[ Object Manager | Empréstimos ]===========================\n"+
                "[ Consultar ]"+
                "\n> Dados do Empréstimo:"+
                "\n- ID: #"+targetEmp.getId()+
                "\n- Pessoa: "+nomePessoa+
                "\n- Objeto: "+nomeObj+
                "\n- Estado: "+empEstado+
                "\n- Data Retirada: "+targetEmp.getDataRetirada()+
                "\n- Data Devolução: "+targetEmp.getDataDevolvido();
                Entrada.leiaBoolean(infoText,"OK","Fechar");
            }    
        }else{
            Entrada.leiaBoolean("Empréstimo não encontrado com os parâmetros informados!","OK","Fechar");
        }
        
        return targetEmp;
    }
    
     private static void AlterarEmprestimo(){
        Emprestimo targetEmp = ConsultarEmprestimo(false);
        
        if(targetEmp != null){
            
            String nomePessoa = "("+String.valueOf(targetEmp.getPessoa())+")";
            Pessoa checkPessoa = PessoaService.get(targetEmp.getPessoa());
            if(checkPessoa != null){ nomePessoa = nomePessoa+" "+checkPessoa.getNome(); }

            String nomeObj = "("+String.valueOf(targetEmp.getObjeto())+")";
            Objeto checkObjeto = ObjetoService.get(targetEmp.getObjeto());
            if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }

            String empEstado = "Devolvido";
            if( targetEmp.isAtivo() ){ empEstado = "Retirado"; }
            
            String alterText = "===========================[ Object Manager | Empréstimos ]===========================\n"+
            "[ Alterações ]"+
            "\n> Dados do Empréstimo:"+
            "\n- ID: #"+targetEmp.getId()+
            "\n- Pessoa: "+nomePessoa+
            "\n- Objeto: "+nomeObj+
            "\n- Estado: "+empEstado+
            "\n- Data Retirada: "+targetEmp.getDataRetirada()+
            "\n- Data Devolução: "+targetEmp.getDataDevolvido()+
            "\n============================="+
            "\n> Indique o que deseja alterar:"+
            "\n1) Estado"+
            "\n\n0) Cancelar";

            boolean exitLoop = false;
            int emprestimoId = targetEmp.getId();

            while (!exitLoop){        
                int option = Entrada.leiaInt(alterText);

                switch (option) {
                    case 0:
                        exitLoop = true;
                        break;
                    case 1:
                        String alterEstado = "===========================[ Object Manager | Empréstimos ]===========================\n"+
                        "[ Alterações ]"+
                        "\n> Indique o Estado do Empréstimo"+
                        "\n0) Devolvido (Atualiza Data de Devolução)"+
                        "\n1) Emprestado";
                        
                        int nEstado = -1;
                        while (nEstado < 0 || nEstado > 1){
                            nEstado = Entrada.leiaInt(alterEstado);
                        }
                        
                        String nEstadoStr = "";
                        switch(nEstado){
                            case 0:
                                nEstadoStr = "Devolvido (A Data de Devolução será atualizada!)";
                                break;
                            case 1:
                                nEstadoStr = "Emprestado";
                                break;
                        }

                        String confirmEstadoText = "===========================[ Object Manager | Empréstimos ]===========================\n"+
                        "[ Alterações ]"+
                        "\n> Confirme os Dados:"+
                        "\n- ID: #"+emprestimoId+
                        "\n- Estado: "+nEstadoStr;

                        boolean confirmEstado = Entrada.leiaBoolean(confirmEstadoText,"Confirmar","Cancelar");
                        if(confirmEstado){
                            exitLoop = true;
                            if ( (nEstado == 1) && (!checkObjeto.isAtivo() || ManutencaoService.inService(targetEmp.getObjeto()) || !EmprestimoService.isAvaliable(targetEmp.getObjeto())) ){
                                Entrada.leiaBoolean("Não é possivel alterar o Estado, pois o Objeto ja está sendo usado/reparado!","OK","Fechar");                       
                            }else{
                                if ( EmprestimoService.update(emprestimoId, "ativo", nEstado) ){
                                    if (nEstado == 0){
                                        EmprestimoService.updateDate(emprestimoId, "data_devolvido");
                                    }
                                    Entrada.leiaBoolean("Empréstimo atualizado com sucesso!","OK","Fechar");
                                }else{
                                    Entrada.leiaBoolean("Não foi possível atualizar os dados do Empréstimo!","OK","Fechar");
                                }
                            }
                            
                        }
                }
            }
        }
    }
    
    private static void ExcluirEmprestimo(){
        Emprestimo targetEmp = ConsultarEmprestimo(false); 
        if(targetEmp != null){
            int emprestimoId = targetEmp.getId();
            
            String nomePessoa = "("+String.valueOf(targetEmp.getPessoa())+")";
            Pessoa checkPessoa = PessoaService.get(targetEmp.getPessoa());
            if(checkPessoa != null){ nomePessoa = nomePessoa+" "+checkPessoa.getNome(); }

            String nomeObj = "("+String.valueOf(targetEmp.getObjeto())+")";
            Objeto checkObjeto = ObjetoService.get(targetEmp.getObjeto());
            if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }

            String empEstado = "Devolvido";
            if( targetEmp.isAtivo() ){ empEstado = "Retirado"; }
            
            String deleteText = "===========================[ Object Manager | Empréstimos ]===========================\n"+
            "[ Excluir ]"+
            "\n> Dados do Empréstimo:"+
            "\n- ID: #"+emprestimoId+
            "\n- Pessoa: "+nomePessoa+
            "\n- Objeto: "+nomeObj+
            "\n- Estado: "+empEstado+
            "\n- Data Retirada: "+targetEmp.getDataRetirada()+
            "\n- Data Devolução: "+targetEmp.getDataDevolvido()+
            "\n============================="+
            "\n> Tem certeza em excluir o Empréstimo #"+emprestimoId+" ?";
            
            boolean confirm = Entrada.leiaBoolean(deleteText,"Confirmar","Cancelar");
            if(confirm){
                if (EmprestimoService.delete(emprestimoId)){
                    Entrada.leiaBoolean("Empréstimo deletado com sucesso!","OK","Fechar");
                }else{
                    Entrada.leiaBoolean("Não foi possível deletar o Empréstimo!","OK","Fechar");
                }

            }
        }
    }
    
    private static void ListarEmprestimos() {
        String listaTipos = "===========================[ Object Manager | Empréstimos ]===========================\n"+
        "ID | PESSOA | OBJETO | ESTADO | RETIRADA | DEVOLUÇÃO\n";
        
        ArrayList<Emprestimo> emprestimos = EmprestimoService.getAll();
        
        for (Emprestimo emp : emprestimos) {
            
            String nomePessoa = "("+String.valueOf(emp.getPessoa())+")";
            Pessoa checkPessoa = PessoaService.get(emp.getPessoa());
            if(checkPessoa != null){ nomePessoa = nomePessoa+" "+checkPessoa.getNome(); }
            
            String nomeObj = "("+String.valueOf(emp.getObjeto())+")";
            Objeto checkObjeto = ObjetoService.get(emp.getObjeto());
            if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }
            
            String empEstado = "Devolvido";
            if( emp.isAtivo() ){ empEstado = "Retirado"; }

            listaTipos += ( "#"+emp.getId()+" | "+nomePessoa+" | "+nomeObj+" | "+empEstado+" | "+emp.getDataRetirada()+" | "+emp.getDataDevolvido()+"\r\n" );
        }
        
        Entrada.leiaBoolean(listaTipos,"OK","Fechar"); 
    }
}
