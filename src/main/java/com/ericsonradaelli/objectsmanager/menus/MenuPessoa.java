package com.ericsonradaelli.objectsmanager.menus;

import java.util.ArrayList;
import com.ericsonradaelli.objectsmanager.helpers.Entrada;
import com.ericsonradaelli.objectsmanager.entities.Pessoa;
import com.ericsonradaelli.objectsmanager.services.PessoaService;

public class MenuPessoa {
    //Completo - Testado
    public static void show() {

        boolean exitMenu = false;

        while (!exitMenu){        
            String menuText = 
                "=============================[ Object Manager | Pessoas ]=============================\n"+
                "1) Incluir Pessoa\n"+
                "2) Consultar Pessoa\n"+
                "3) Alterar Pessoa\n"+
                "4) Excluir Pessoa\n"+
                "5) Listar Pessoas\n"+
                "\n0) Menu Principal";

            int option = Entrada.leiaInt(menuText);

            switch (option) {
                case 0:
                    exitMenu = true;
                    break;
                case 1:
                    CadastrarPessoa();
                    break;
                case 2:
                    ConsultarPessoa(true);
                    break;
                case 3:
                    AlterarPessoa();
                    break;
                case 4:
                    ExcluirPessoa();
                    break;
                case 5:
                    ListarPessoas();
                    break;
            }
            
        }
    }

    private static void CadastrarPessoa(){
        
        String pDoc = Entrada.leiaString("=============================[ Object Manager | Pessoas ]=============================\nCadastro (1/4)\n> Indique o CPF do Usuário\n");
        pDoc = pDoc.replaceAll("\\s","").toUpperCase();
       
        String pNome = Entrada.leiaString("=============================[ Object Manager | Pessoas ]=============================\nCadastro (2/4)\n> Indique o Nome do Usuário\n");
        pNome = pNome.toUpperCase(); //RS001

        String pTelefone = Entrada.leiaString("=============================[ Object Manager | Pessoas ]=============================\nCadastro (3/4)\n> Indique o Telefone do Usuário\n");
        pTelefone = pTelefone.replaceAll("\\s","").toUpperCase();
        
        String confirmText = "=============================[ Object Manager | Pessoas ]=============================\n"+
        "Cadastro (4/4)"+
        "\n> Confirme os Dados:"+
        "\n- CPF: "+pDoc+
        "\n- Nome: "+pNome+
        "\n- Telefone: "+pTelefone;

        boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
        if(confirm){
           
            int statusCode = PessoaService.cadastro(pDoc, pNome, pTelefone);
            String message = "Erro não especificado.";
            
            switch(statusCode){
                case 0:
                    message = "O Usuário foi cadastrado com sucesso!";
                    break;
                case 1:
                    message = "Não são permitidos campos vazios!";
                    break;
                case 2:
                    message = "O CPF informado já está cadastrado!";
                    break;
                default:
                    message = "Erro no banco de dados!";
                    break;
            }
            
            Entrada.leiaBoolean(message,"OK","Fechar");
        }          

    }

    private static Pessoa ConsultarPessoa(boolean showInfo){
        Pessoa targetPessoa;
        
        boolean findById = Entrada.leiaBoolean("=============================[ Object Manager | Pessoas ]=============================\n[ Buscar ]\n> Com qual dado deseja procurar?\n","Identificador","Documento");
        if(findById){
            int pId = Entrada.leiaInt("=============================[ Object Manager | Pessoas ]=============================\n[ Buscar ]\n> Indique o ID do Usuário\n");
            targetPessoa = PessoaService.get(pId);
        }else{
            String pDoc = Entrada.leiaString("=============================[ Object Manager | Pessoas ]=============================\n[ Buscar ]\n> Indique o Documento do Usuário\n");
            targetPessoa = PessoaService.get(pDoc);
        }
        
        if(targetPessoa != null){
            if(showInfo){
                String infoText = "=============================[ Object Manager | Pessoas ]=============================\n"+
                "[ Consultar ]"+
                "\n> Dados do Usuário:"+
                "\n- ID: #"+targetPessoa.getId()+
                "\n- CPF: "+targetPessoa.getDocumento()+
                "\n- Nome: "+targetPessoa.getNome()+
                "\n- Telefone: "+targetPessoa.getTelefone();
                Entrada.leiaBoolean(infoText,"OK","Fechar");
            }    
        }else{
            Entrada.leiaBoolean("Usuário não encontrado com os parâmetros informados!","OK","Fechar");
        }
        
        return targetPessoa;
    }

    private static void AlterarPessoa(){
        Pessoa targetPessoa = ConsultarPessoa(false);
        
        if(targetPessoa != null){
            String alterText = "=============================[ Object Manager | Pessoas ]=============================\n"+
            "[ Alterações ]"+
            "\n> Dados do Usuário:"+
            "\n- ID: #"+targetPessoa.getId()+
            "\n- CPF: "+targetPessoa.getDocumento()+
            "\n- Nome: "+targetPessoa.getNome()+
            "\n- Telefone: "+targetPessoa.getTelefone()+
            "\n============================="+
            "\n> Indique o que deseja alterar:"+
            "\n1) Nome"+
            "\n2) Telefone"+
            "\n\n0) Cancelar";

            boolean exitLoop = false;
            int pessoaId = targetPessoa.getId();

            while (!exitLoop){        
                int option = Entrada.leiaInt(alterText);

                switch (option) {
                    case 0:
                        exitLoop = true;
                        break;
                    case 1: 
                        String nNome = Entrada.leiaString("=============================[ Object Manager | Pessoas ]=============================\n[ Alterações ]\n> Indique o Nome do Usuário\n",targetPessoa.getNome());
                        nNome = nNome.toUpperCase(); //RS001    
                        if(!nNome.isBlank()){
                            String confirmText = "=============================[ Object Manager | Pessoas ]=============================\n"+
                            "[ Alterações ]"+
                            "\n> Confirme os Dados:"+
                            "\n- ID: #"+pessoaId+
                            "\n- Nome: "+nNome;
                            
                            boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
                            if(confirm){
                                exitLoop = true;
                                if ( PessoaService.update(targetPessoa.getId(), "nome", nNome) ){
                                    Entrada.leiaBoolean("Dado do Usuário atualizado com sucesso!","OK","Fechar");
                                }else{
                                    Entrada.leiaBoolean("Não foi possível atualizar os dados do Usuário!","OK","Fechar");
                                }
                            }
                        }else{
                            Entrada.leiaBoolean("O campo não pode estar vazio!","OK","Fechar");
                        }
                        break;
                    case 2:
                        String nTelefone = Entrada.leiaString("=============================[ Object Manager | Pessoas ]=============================\n[ Alterações ]\n> Indique o Telefone do Usuário\n",targetPessoa.getTelefone());
                        nTelefone = nTelefone.replaceAll("\\s","").toUpperCase();
                        if(!nTelefone.isBlank()){
                            String confirmText = "=============================[ Object Manager | Pessoas ]=============================\n"+
                            "[ Alterações ]"+
                            "\n> Confirme os Dados:"+
                            "\n- ID: #"+pessoaId+
                            "\n- Telefone: "+nTelefone;
                            
                            boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
                            if(confirm){
                                exitLoop = true;
                                if ( PessoaService.update(targetPessoa.getId(), "telefone", nTelefone) ){
                                    Entrada.leiaBoolean("Dado do Usuário atualizado com sucesso!","OK","Fechar");
                                }else{
                                    Entrada.leiaBoolean("Não foi possível atualizar os dados do Usuário!","OK","Fechar");
                                }
                            }
                        }else{
                            Entrada.leiaBoolean("O campo não pode estar vazio!","OK","Fechar");
                        }
                        break;
                }

            }
        }
    }

    private static void ExcluirPessoa(){
        Pessoa targetPessoa = ConsultarPessoa(false); 
        if(targetPessoa != null){
            int pessoaId = targetPessoa.getId();
            String deleteText = "=============================[ Object Manager | Pessoas ]=============================\n"+
            "[ Excluir ]"+
            "\n> Dados do Usuário:"+
            "\n- ID: #"+pessoaId+
            "\n- CPF: "+targetPessoa.getDocumento()+
            "\n- Nome: "+targetPessoa.getNome()+
            "\n- Telefone: "+targetPessoa.getTelefone()+
            "\n============================="+
            "\n> Tem certeza em excluir o Usuário #"+pessoaId+" ?\n> TODOS os dados relacionados (empréstimos) serão apagados!";
            
            boolean confirm = Entrada.leiaBoolean(deleteText,"Confirmar","Cancelar");
            if(confirm){
                if (PessoaService.delete(pessoaId)){
                    Entrada.leiaBoolean("Usuário deletado com sucesso!","OK","Fechar");
                }else{
                    Entrada.leiaBoolean("Não foi possível deletar o Usuário!","OK","Fechar");
                }

            }
        }
    }

    private static void ListarPessoas(){
        String listaPessoas = "=============================[ Object Manager | Pessoas ]=============================\n"+
        "ID | CPF | NOME | TELEFONE\n";
        
        ArrayList<Pessoa> pessoas = PessoaService.getAll();

        for (Pessoa pessoa : pessoas) {
            listaPessoas += ( "#"+pessoa.getId()+" | "+pessoa.getDocumento()+" | "+pessoa.getNome()+" | "+pessoa.getTelefone()+"\r\n" );
        }
        
        Entrada.leiaBoolean(listaPessoas,"OK","Fechar"); 
    }
    
}
